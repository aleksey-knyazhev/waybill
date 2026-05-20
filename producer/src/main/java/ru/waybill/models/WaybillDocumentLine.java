package ru.waybill.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WaybillDocumentLine {
    @NotNull
    private Integer lineNumber;

    @Valid
    @NotNull
    private Item item;

    @NotNull
    private BigDecimal quantity;

    private BigDecimal unitPrice;
    private BigDecimal amountWithoutTax;
    private BigDecimal taxAmount;
    private BigDecimal amountWithTax;
}
