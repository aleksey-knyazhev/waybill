package ru.waybill.models;

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
    private Integer lineNumber;
    private Item item;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amountWithoutTax;
    private BigDecimal taxAmount;
    private BigDecimal amountWithTax;

}
