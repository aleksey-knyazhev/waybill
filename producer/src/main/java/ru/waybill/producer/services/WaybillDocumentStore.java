package ru.waybill.producer.services;

import org.springframework.stereotype.Service;
import ru.waybill.producer.models.WaybillDocument;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class WaybillDocumentStore {
    private final AtomicReference<WaybillDocument> document = new AtomicReference<>(new WaybillDocument());

    public WaybillDocument getDocument() {
        return document.get();
    }

    public void setDocument(WaybillDocument document) {
        this.document.set(document);
    }
}
