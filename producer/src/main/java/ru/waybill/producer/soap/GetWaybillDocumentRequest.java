package ru.waybill.producer.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "getWaybillDocumentRequest", namespace = SoapNamespaces.WAYBILL)
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class GetWaybillDocumentRequest {
    @XmlElement(name = "invoice_number", namespace = SoapNamespaces.WAYBILL, required = true)
    private String invoiceNumber;

    @XmlElement(name = "invoice_date", namespace = SoapNamespaces.WAYBILL, required = true)
    private String invoiceDate;
}
