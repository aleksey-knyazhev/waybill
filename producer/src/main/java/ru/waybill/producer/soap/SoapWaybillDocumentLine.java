package ru.waybill.producer.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
