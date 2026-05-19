package ru.waybill.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import ru.waybill.models.WaybillDocument;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
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

    public static SoapWaybillDocument from(WaybillDocument document) {
        SoapWaybillDocument result = new SoapWaybillDocument();
        result.invoiceNumber = document.getInvoiceNumber();
        result.invoiceDate = document.getInvoiceDate() == null ? null : document.getInvoiceDate().toString();
        result.status = document.getStatus();
        result.seller = SoapOrganization.from(document.getSeller());
        result.buyer = SoapOrganization.from(document.getBuyer());
        result.currencyName = document.getCurrencyName();
        result.currencyCode = document.getCurrencyCode();
        result.transferBasis = document.getTransferBasis();
        result.lines = document.getLines().stream()
                .map(SoapWaybillDocumentLine::from)
                .toList();
        return result;
    }
}
