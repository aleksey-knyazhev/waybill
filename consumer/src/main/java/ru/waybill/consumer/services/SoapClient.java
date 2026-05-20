package ru.waybill.consumer.services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.ws.BindingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.waybill.consumer.soap.generated.GetWaybillDocumentRequest;
import ru.waybill.consumer.soap.generated.GetWaybillDocumentResponse;
import ru.waybill.consumer.soap.generated.WaybillDocument;
import ru.waybill.consumer.soap.generated.WaybillPortType;
import ru.waybill.consumer.soap.generated.WaybillService;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class SoapClient {
    private static final String SOAP_ENVELOPE_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";

    private final WaybillPortType waybillPort;
    private final String envelopeTemplate;

    public SoapClient(@Value("${producer.soap.wsdl-url}") String wsdlUrl) throws IOException {
        WaybillService service = new WaybillService(new URL(wsdlUrl));
        this.waybillPort = service.getWaybillPort();
        ((BindingProvider) waybillPort).getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceEndpoint(wsdlUrl));
        this.envelopeTemplate = loadEnvelopeTemplate();
    }

    public String getWaybillDocument() {
        GetWaybillDocumentResponse response = getWaybillDocumentResponse();
        return envelope(marshal(response));
    }

    public String getWaybillDocumentView(String xsltVersion) {
        return withStylesheet(getWaybillDocument(), stylesheetHref(xsltVersion));
    }

    public WaybillDocument getWaybillDocumentObject() {
        return getWaybillDocumentResponse().getWaybillDocument();
    }

    private GetWaybillDocumentResponse getWaybillDocumentResponse() {
        return waybillPort.getWaybillDocument(new GetWaybillDocumentRequest());
    }

    private String marshal(GetWaybillDocumentResponse response) {
        try {
            JAXBContext context = JAXBContext.newInstance(GetWaybillDocumentResponse.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            StringWriter writer = new StringWriter();
            marshaller.marshal(response, writer);
            return writer.toString();
        } catch (JAXBException exception) {
            throw new IllegalStateException("Cannot marshal SOAP response", exception);
        }
    }

    private String envelope(String body) {
        return envelopeTemplate
                .replace("${soapEnvelopeNamespace}", SOAP_ENVELOPE_NAMESPACE)
                .replace("${body}", body);
    }

    private String withStylesheet(String xml, String stylesheetHref) {
        return xml.replaceFirst(
                "\\?>",
                "?>\n<?xml-stylesheet type=\"text/xsl\" href=\"" + stylesheetHref + "\"?>"
        );
    }

    private String stylesheetHref(String xsltVersion) {
        return switch (xsltVersion) {
            case "02" -> "/xslt/waybill-document_version_02.xsl";
            default -> "/xslt/waybill-document_version_01.xsl";
        };
    }

    private String loadEnvelopeTemplate() throws IOException {
        try (InputStream inputStream = new ClassPathResource("soap/waybill-envelope.xml").getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String serviceEndpoint(String wsdlUrl) {
        int queryIndex = wsdlUrl.indexOf('?');
        if (queryIndex >= 0) {
            return wsdlUrl.substring(0, queryIndex);
        }
        if (wsdlUrl.endsWith(".wsdl")) {
            return wsdlUrl.substring(0, wsdlUrl.length() - ".wsdl".length());
        }
        return wsdlUrl;
    }
}
