package ru.waybill.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.waybill.models.WaybillDocument;
import ru.waybill.services.WaybillDocumentStore;

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
