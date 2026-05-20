package ru.waybill.producer.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getWaybillDocumentRequest", namespace = SoapNamespaces.WAYBILL)
@XmlAccessorType(XmlAccessType.FIELD)
public class GetWaybillDocumentRequest {
}
