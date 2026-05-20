package ru.waybill.producer.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "getWaybillDocumentResponse", namespace = SoapNamespaces.WAYBILL)
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetWaybillDocumentResponse {
    @XmlElement(name = "waybillDocument", namespace = SoapNamespaces.WAYBILL)
    private SoapWaybillDocument waybillDocument;
}
