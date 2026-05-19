package ru.waybill.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import ru.waybill.models.WaybillDocumentLine;

import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class SoapWaybillDocumentLine {
    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private Integer lineNumber;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private SoapItem item;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private BigDecimal quantity;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private BigDecimal unitPrice;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private BigDecimal amountWithoutTax;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private BigDecimal taxAmount;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private BigDecimal amountWithTax;

    public static SoapWaybillDocumentLine from(WaybillDocumentLine line) {
        SoapWaybillDocumentLine result = new SoapWaybillDocumentLine();
        result.lineNumber = line.getLineNumber();
        result.item = SoapItem.from(line.getItem());
        result.quantity = line.getQuantity();
        result.unitPrice = line.getUnitPrice();
        result.amountWithoutTax = line.getAmountWithoutTax();
        result.taxAmount = line.getTaxAmount();
        result.amountWithTax = line.getAmountWithTax();
        return result;
    }
}
