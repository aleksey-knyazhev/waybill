package ru.waybill.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.waybill.models.WaybillDocument;
import ru.waybill.helpers.WaybillDocumentExamples;

@RestController
public class WaybillDocumentController {
    @GetMapping("/api/waybill/example")
    public WaybillDocument getExampleDocument() {
        return WaybillDocumentExamples.createExampleDocument();
    }
}
