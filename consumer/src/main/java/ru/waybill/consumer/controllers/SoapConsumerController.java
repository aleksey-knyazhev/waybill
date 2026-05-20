package ru.waybill.consumer.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.waybill.consumer.services.SoapClient;

@RestController
public class SoapConsumerController {
    private final SoapClient soapClient;

    public SoapConsumerController(SoapClient soapClient) {
        this.soapClient = soapClient;
    }

    @GetMapping(value = "/api/producer/waybill", produces = MediaType.TEXT_XML_VALUE)
    public String getWaybillDocument() {
        return soapClient.getWaybillDocument();
    }
}
