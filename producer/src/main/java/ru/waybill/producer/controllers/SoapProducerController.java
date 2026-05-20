package ru.waybill.producer.controllers;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import ru.waybill.producer.models.WaybillDocument;
import ru.waybill.producer.services.WaybillDocumentStore;
import ru.waybill.producer.soap.GetWaybillDocumentRequest;
import ru.waybill.producer.soap.GetWaybillDocumentResponse;
import ru.waybill.producer.soap.SoapNamespaces;
import ru.waybill.producer.soap.WaybillSoapMapper;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@RestController
public class SoapProducerController {
    private final WaybillDocumentStore documentStore;
    private final WaybillSoapMapper waybillSoapMapper;
    private final Validator validator;
    private final JAXBContext jaxbContext;
    private final String envelopeTemplate;

    public SoapProducerController(
            WaybillDocumentStore documentStore,
            WaybillSoapMapper waybillSoapMapper,
            Validator validator
    ) throws JAXBException, IOException {
        this.documentStore = documentStore;
        this.waybillSoapMapper = waybillSoapMapper;
        this.validator = validator;
        this.jaxbContext = JAXBContext.newInstance(GetWaybillDocumentRequest.class, GetWaybillDocumentResponse.class);
        this.envelopeTemplate = loadEnvelopeTemplate();
    }

    @PostMapping(
            value = "/soap/waybill",
            consumes = {MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = MediaType.TEXT_XML_VALUE
    )
    public String getWaybillDocument(@RequestBody String requestBody) {
        readRequest(requestBody);
        WaybillDocument document = documentStore.getDocument();
        validateDocument(document);
        return envelope(marshal(waybillSoapMapper.toResponse(document)));
    }

    private void readRequest(String requestBody) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.unmarshal(payload(requestBody));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid SOAP request", exception);
        }
    }

    private String marshal(GetWaybillDocumentResponse response) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            StringWriter writer = new StringWriter();
            marshaller.marshal(response, writer);
            return writer.toString();
        } catch (JAXBException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot marshal SOAP response", exception);
        }
    }

    private Node payload(String requestBody) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        disableExternalEntities(factory);

        Document document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(stripByteOrderMark(requestBody))));
        Node body = document.getElementsByTagNameNS(SoapNamespaces.SOAP_ENVELOPE, "Body").item(0);
        if (body == null) {
            throw new IOException("SOAP Body is missing");
        }

        for (int index = 0; index < body.getChildNodes().getLength(); index++) {
            Node node = body.getChildNodes().item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node;
            }
        }
        throw new IOException("SOAP payload is missing");
    }

    private void disableExternalEntities(DocumentBuilderFactory factory) throws ParserConfigurationException {
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
    }

    private String envelope(String body) {
        return envelopeTemplate
                .replace("${soapEnvelopeNamespace}", SoapNamespaces.SOAP_ENVELOPE)
                .replace("${body}", body);
    }

    private String loadEnvelopeTemplate() throws IOException {
        try (InputStream inputStream = new ClassPathResource("soap/waybill-envelope.xml").getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void validateDocument(WaybillDocument document) {
        Set<ConstraintViolation<WaybillDocument>> violations = validator.validate(document);
        if (!violations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "WaybillDocument is not ready: required fields are missing"
            );
        }
    }

    private String stripByteOrderMark(String value) {
        if (value == null || value.isEmpty() || value.charAt(0) != '\uFEFF') {
            return value;
        }
        return value.substring(1);
    }
}
