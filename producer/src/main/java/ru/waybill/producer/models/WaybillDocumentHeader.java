package ru.waybill.producer.models;

import java.time.LocalDate;

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
