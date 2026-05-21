package ru.waybill.producer.services;

import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public WaybillDocument setDocument(WaybillDocument document) {
        WaybillDocument target = repository
                .findByInvoiceNumberAndInvoiceDate(document.getInvoiceNumber(), document.getInvoiceDate())
                .orElse(document);

        if (target != document) {
            updateDocument(target, document);
        }

        return repository.save(target);
    }

    private void updateDocument(WaybillDocument target, WaybillDocument source) {
        target.setStatus(source.getStatus());
        target.setSeller(source.getSeller());
        target.setBuyer(source.getBuyer());
        target.setCurrencyName(source.getCurrencyName());
        target.setCurrencyCode(source.getCurrencyCode());
        target.setTransferBasis(source.getTransferBasis());
        target.getLines().clear();
        target.getLines().addAll(source.getLines());
    }
}
