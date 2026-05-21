package ru.waybill.consumer.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.waybill.consumer.services.SoapClient;
import ru.waybill.consumer.soap.generated.WaybillDocument;

@RestController
public class WaybillViewController {
    private final SoapClient soapClient;

    public WaybillViewController(SoapClient soapClient) {
        this.soapClient = soapClient;
    }

    @GetMapping(value = "/api/xsd/waybill-document", produces = MediaType.APPLICATION_JSON_VALUE)
    public WaybillDocument getWaybillDocument() {
        return soapClient.getWaybillDocumentObject();
    }

    @GetMapping(value = "/import/waybill-document", produces = MediaType.TEXT_XML_VALUE)
    public String getWaybillDocumentView(
            @RequestParam(name = "xslt", defaultValue = "01") String xsltVersion
    ) {
        return soapClient.getWaybillDocumentView(xsltVersion);
    }
}
