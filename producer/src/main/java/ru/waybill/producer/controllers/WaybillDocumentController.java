package ru.waybill.producer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.waybill.producer.models.WaybillDocument;
import ru.waybill.producer.services.WaybillDocumentStore;

@RestController
public class WaybillDocumentController {
    private final WaybillDocumentStore documentStore;

    public WaybillDocumentController(WaybillDocumentStore documentStore) {
        this.documentStore = documentStore;
    }

    @GetMapping("/api/waybill/example")
    public WaybillDocument getExampleDocument() {
        return documentStore.getDocument();
    }
}
