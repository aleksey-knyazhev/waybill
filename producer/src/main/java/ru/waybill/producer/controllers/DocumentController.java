package ru.waybill.producer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.waybill.producer.models.WaybillDocument;
import ru.waybill.producer.services.WaybillDocumentStore;

@RestController
public class DocumentController {
    private final WaybillDocumentStore documentStore;

    public DocumentController(WaybillDocumentStore documentStore) {
        this.documentStore = documentStore;
    }

    @GetMapping("/api/waybill/example")
    public WaybillDocument getExampleDocument() {
        return documentStore.getDocument();
    }
}
