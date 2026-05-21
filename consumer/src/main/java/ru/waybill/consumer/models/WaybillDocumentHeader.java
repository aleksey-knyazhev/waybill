package ru.waybill.consumer.models;

import java.time.LocalDate;

public record WaybillDocumentHeader(
        Long id,
        String invoiceNumber,
        LocalDate invoiceDate,
        Integer status,
        OrganizationHeader seller,
        OrganizationHeader buyer,
        String currencyName,
        String currencyCode,
        String transferBasis
) {
}
