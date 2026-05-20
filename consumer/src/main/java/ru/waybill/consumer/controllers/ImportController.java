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
public class ImportController {
    private final SoapClient soapClient;

    public ImportController(SoapClient soapClient) {
        this.soapClient = soapClient;
    }

    @GetMapping(value = "/api/xsd/waybill-document", produces = MediaType.APPLICATION_JSON_VALUE)
    public WaybillDocument getWaybillDocument() {
        return soapClient.getWaybillDocumentObject();
    }

    @GetMapping(value = "/import/waybill-document", produces = MediaType.TEXT_XML_VALUE)
    public String getWaybillDocumentView() {
        return soapClient.getWaybillDocumentView();
    }

    @GetMapping(value = "/xslt/waybill-document_version_01.xsl", produces = MediaType.TEXT_XML_VALUE)
    public String getWaybillDocumentXslt() throws IOException {
        return readXslt("xslt/waybill-document_version_01.xsl");
    }

    @GetMapping(value = "/xslt/waybill-document_version_02.xsl", produces = MediaType.TEXT_XML_VALUE)
    public String getWaybillDocumentXsltVersion02() throws IOException {
        return readXslt("xslt/waybill-document_version_02.xsl");
    }

    private String readXslt(String path) throws IOException {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
