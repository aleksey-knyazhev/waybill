package ru.waybill.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private String invoiceNumber;

    @NotNull
    private LocalDate invoiceDate;

    private Integer status;

    @Valid
    @NotNull
    private Organization seller;

    @Valid
    @NotNull
    private Organization buyer;

    private String currencyName;
    private String currencyCode;
    private String transferBasis;

    @Valid
    @NotNull
    private List<WaybillDocumentLine> lines = new ArrayList<>();
}
