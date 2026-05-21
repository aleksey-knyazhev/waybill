package ru.waybill.consumer.controllers;

import org.springframework.http.MediaType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.waybill.consumer.services.SoapClient;
import ru.waybill.consumer.soap.generated.WaybillDocument;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
public class WaybillViewController {
    private final SoapClient soapClient;
    private final String waybillDocumentPage;

    public WaybillViewController(SoapClient soapClient) throws IOException {
        this.soapClient = soapClient;
        this.waybillDocumentPage = loadWaybillDocumentPage();
    }

    @GetMapping(value = "/api/xsd/waybill-document", produces = MediaType.APPLICATION_JSON_VALUE)
    public WaybillDocument getWaybillDocument() {
        return soapClient.getWaybillDocumentObject();
    }

    @GetMapping(value = "/api/xml/waybill-document", produces = MediaType.TEXT_XML_VALUE)
    public String getWaybillDocumentXml() {
        return soapClient.getWaybillDocument();
    }

    @GetMapping(value = "/import/waybill-document", produces = MediaType.TEXT_HTML_VALUE)
    public String getWaybillDocumentView() {
        return waybillDocumentPage;
    }

    private String loadWaybillDocumentPage() throws IOException {
        try (InputStream inputStream = new ClassPathResource("frontend/waybill-document.html").getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
