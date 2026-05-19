package ru.waybill.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import ru.waybill.models.WaybillDocument;

@XmlRootElement(name = "getWaybillDocumentResponse", namespace = SoapNamespaces.WAYBILL)
@XmlAccessorType(XmlAccessType.FIELD)
public class GetWaybillDocumentResponse {
    @XmlElement(name = "waybillDocument", namespace = SoapNamespaces.WAYBILL)
    private SoapWaybillDocument waybillDocument;

    public GetWaybillDocumentResponse() {
    }

    public GetWaybillDocumentResponse(SoapWaybillDocument waybillDocument) {
        this.waybillDocument = waybillDocument;
    }

    public static GetWaybillDocumentResponse from(WaybillDocument document) {
        return new GetWaybillDocumentResponse(SoapWaybillDocument.from(document));
    }

    public SoapWaybillDocument getWaybillDocument() {
        return waybillDocument;
    }

    public void setWaybillDocument(SoapWaybillDocument waybillDocument) {
        this.waybillDocument = waybillDocument;
    }
}
