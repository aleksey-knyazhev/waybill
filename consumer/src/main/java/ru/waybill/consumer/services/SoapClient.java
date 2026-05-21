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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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

    public String getWaybillDocument(String invoiceNumber, String invoiceDate) {
        GetWaybillDocumentResponse response = getWaybillDocumentResponse(invoiceNumber, invoiceDate);
        return envelope(marshal(response));
    }

    public WaybillDocument getWaybillDocumentObject(String invoiceNumber, String invoiceDate) {
        return getWaybillDocumentResponse(invoiceNumber, invoiceDate).getWaybillDocument();
    }

    private GetWaybillDocumentResponse getWaybillDocumentResponse(String invoiceNumber, String invoiceDate) {
        GetWaybillDocumentRequest request = new GetWaybillDocumentRequest();
        request.setInvoiceNumber(invoiceNumber);
        request.setInvoiceDate(xmlDate(invoiceDate));
        return waybillPort.getWaybillDocument(request);
    }

    private XMLGregorianCalendar xmlDate(String invoiceDate) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(invoiceDate);
        } catch (DatatypeConfigurationException exception) {
            throw new IllegalStateException("Cannot create XML date converter", exception);
        }
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
