package ru.waybill.controllers;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import ru.waybill.services.WaybillDocumentStore;
import ru.waybill.soap.GetWaybillDocumentRequest;
import ru.waybill.soap.GetWaybillDocumentResponse;
import ru.waybill.soap.SoapNamespaces;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@RestController
public class SoapWaybillController {
    private final WaybillDocumentStore documentStore;
    private final JAXBContext jaxbContext;

    public SoapWaybillController(WaybillDocumentStore documentStore) throws JAXBException {
        this.documentStore = documentStore;
        this.jaxbContext = JAXBContext.newInstance(GetWaybillDocumentRequest.class, GetWaybillDocumentResponse.class);
    }

    @PostMapping(
            value = "/soap/waybill",
            consumes = {MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = MediaType.TEXT_XML_VALUE
    )
    public String getWaybillDocument(@RequestBody String requestBody) {
        readRequest(requestBody);
        return envelope(marshal(GetWaybillDocumentResponse.from(documentStore.getDocument())));
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
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope xmlns:soap="%s">
                    <soap:Body>
                        %s
                    </soap:Body>
                </soap:Envelope>
                """.formatted(SoapNamespaces.SOAP_ENVELOPE, body);
    }

    private String stripByteOrderMark(String value) {
        if (value == null || value.isEmpty() || value.charAt(0) != '\uFEFF') {
            return value;
        }
        return value.substring(1);
    }
}
