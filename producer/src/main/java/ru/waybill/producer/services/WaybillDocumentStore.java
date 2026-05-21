package ru.waybill.producer.services;

import org.springframework.stereotype.Service;
import ru.waybill.producer.models.WaybillDocument;
import ru.waybill.producer.repositories.WaybillDocumentRepository;

@Service
public class WaybillDocumentStore {
    private final WaybillDocumentRepository repository;

    public WaybillDocumentStore(WaybillDocumentRepository repository) {
        this.repository = repository;
    }

    public WaybillDocument getDocument() {
        return repository.findTopByOrderByIdDesc().orElseGet(WaybillDocument::new);
    }

    public void setDocument(WaybillDocument document) {
        repository.save(document);
    }
}
