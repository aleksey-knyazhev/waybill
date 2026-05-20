package ru.waybill.consumer.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringReader;

@Service
public class SoapClient {
    private static final String SOAP_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final String WSDL_SOAP_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/soap/";
    private static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    private static final String REQUEST_BODY = """
            <?xml version="1.0" encoding="UTF-8"?>
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                              xmlns:way="http://waybill.ru/soap">
                <soapenv:Header/>
                <soapenv:Body>
                    <way:getWaybillDocumentRequest/>
                </soapenv:Body>
            </soapenv:Envelope>
            """;

    private final RestClient restClient;
    private final String wsdlUrl;
    private volatile SoapContract soapContract;
    private volatile Schema waybillSchema;

    public SoapClient(
            @Value("${producer.soap.wsdl-url}") String wsdlUrl
    ) {
        this.restClient = RestClient.create();
        this.wsdlUrl = wsdlUrl;
    }

    public String getWaybillDocument() {
        SoapContract contract = contract();
        validatePayload(REQUEST_BODY);

        String response = restClient.post()
                .uri(contract.serviceLocation())
                .contentType(MediaType.TEXT_XML)
                .accept(MediaType.TEXT_XML)
                .header("SOAPAction", contract.soapAction())
                .body(REQUEST_BODY)
                .retrieve()
                .body(String.class);

        validatePayload(response);
        return response;
    }

    private SoapContract contract() {
        SoapContract current = soapContract;
        if (current != null) {
            return current;
        }

        String wsdl = restClient.get()
                .uri(wsdlUrl)
                .accept(MediaType.TEXT_XML)
                .retrieve()
                .body(String.class);
        SoapContract parsed = parseWsdl(wsdl);
        soapContract = parsed;
        return parsed;
    }

    private SoapContract parseWsdl(String wsdl) {
        Document document = parseXml(wsdl);
        String serviceLocation = attribute(document, WSDL_SOAP_NAMESPACE, "address", "location");
        String soapAction = attribute(document, WSDL_SOAP_NAMESPACE, "operation", "soapAction");
        String xsdLocation = attribute(document, XSD_NAMESPACE, "import", "schemaLocation");
        return new SoapContract(serviceLocation, soapAction, xsdLocation);
    }

    private void validatePayload(String soapEnvelope) {
        try {
            Schema schema = schema();
            Node payload = payload(soapEnvelope);
            schema.newValidator().validate(new DOMSource(payload));
        } catch (SAXException | IOException exception) {
            throw new IllegalStateException("SOAP payload does not match producer XSD", exception);
        }
    }

    private Schema schema() throws SAXException {
        Schema current = waybillSchema;
        if (current != null) {
            return current;
        }

        String xsd = restClient.get()
                .uri(contract().xsdLocation())
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .body(String.class);
        Schema parsed = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                .newSchema(new StreamSource(new StringReader(xsd)));
        waybillSchema = parsed;
        return parsed;
    }

    private Node payload(String soapEnvelope) {
        Document document = parseXml(soapEnvelope);
        Node body = document.getElementsByTagNameNS(SOAP_NAMESPACE, "Body").item(0);
        if (body == null) {
            throw new IllegalStateException("SOAP Body is missing");
        }

        for (int index = 0; index < body.getChildNodes().getLength(); index++) {
            Node node = body.getChildNodes().item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node;
            }
        }
        throw new IllegalStateException("SOAP payload is missing");
    }

    private String attribute(Document document, String namespace, String elementName, String attributeName) {
        Node node = document.getElementsByTagNameNS(namespace, elementName).item(0);
        if (node == null || node.getAttributes().getNamedItem(attributeName) == null) {
            throw new IllegalStateException("WSDL is missing " + elementName + " " + attributeName);
        }
        return node.getAttributes().getNamedItem(attributeName).getNodeValue();
    }

    private Document parseXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            disableExternalEntities(factory);
            return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot parse SOAP metadata XML", exception);
        }
    }

    private void disableExternalEntities(DocumentBuilderFactory factory) throws ParserConfigurationException {
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
    }

    private record SoapContract(String serviceLocation, String soapAction, String xsdLocation) {
    }
}
