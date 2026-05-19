package ru.waybill.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WaybillDocument {
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private Integer status;
    private Organization seller;
    private Organization buyer;
    private String currencyName;
    private String currencyCode;
    private String transferBasis;
    private List<WaybillDocumentLine> lines = new ArrayList<>();
}
