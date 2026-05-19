package ru.waybill.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.waybill.models.WaybillDocument;

@RestController
public class WaybillDocumentController {
    @GetMapping("/api/waybill/example")
    public WaybillDocument getExampleDocument() {
        return new WaybillDocument();
    }
}
