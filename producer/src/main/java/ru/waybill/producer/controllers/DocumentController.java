package ru.waybill.producer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.waybill.producer.models.WaybillDocument;
import ru.waybill.producer.models.WaybillDocumentHeader;
import ru.waybill.producer.services.WaybillDocumentService;

import java.util.List;

@RestController
public class DocumentController {
    private final WaybillDocumentService documentService;

    public DocumentController(WaybillDocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/api/waybill/example")
    public WaybillDocument getExampleDocument() {
        return documentService.getDocument();
    }

    @GetMapping("/api/waybill/documents/headers")
    public List<WaybillDocumentHeader> getDocumentsHeaders() {
        return documentService.getDocumentsHeaders();
    }
}
