package ru.waybill.producer.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.waybill.producer.mappers.WaybillDocumentMapper;
import ru.waybill.producer.models.WaybillDocument;
import ru.waybill.producer.models.Organization;
import ru.waybill.producer.repositories.WaybillDocumentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WaybillDocumentService {
    private final WaybillDocumentRepository repository;
    private final WaybillDocumentMapper mapper;

    public WaybillDocumentService(WaybillDocumentRepository repository, WaybillDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public WaybillDocument getDocument() {
        return repository.findTopByOrderByIdDesc().orElseGet(WaybillDocument::new);
    }

    @Transactional(readOnly = true)
    public Optional<WaybillDocument> findDocument(String invoiceNumber, LocalDate invoiceDate) {
        return repository.findByInvoiceNumberAndInvoiceDate(invoiceNumber, invoiceDate);
    }

    @Transactional(readOnly = true)
    public List<WaybillDocumentHeader> getDocumentsHeaders() {
        return mapper.toHeaders(repository.findAllByOrderByInvoiceDateDescInvoiceNumberAsc());
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
        mapper.updateDocument(target, source);
    }

    public record WaybillDocumentHeader(
            Long id,
            String invoiceNumber,
            LocalDate invoiceDate,
            Integer status,
            Organization seller,
            Organization buyer,
            String currencyName,
            String currencyCode,
            String transferBasis
    ) {
    }
}
