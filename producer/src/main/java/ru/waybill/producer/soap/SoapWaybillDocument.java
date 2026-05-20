package ru.waybill.producer.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoapWaybillDocument {
    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String invoiceNumber;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String invoiceDate;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private Integer status;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private SoapOrganization seller;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private SoapOrganization buyer;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String currencyName;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String currencyCode;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String transferBasis;

    @XmlElementWrapper(name = "lines", namespace = SoapNamespaces.WAYBILL)
    @XmlElement(name = "line", namespace = SoapNamespaces.WAYBILL)
    private List<SoapWaybillDocumentLine> lines = new ArrayList<>();
}
