package ru.waybill.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import ru.waybill.models.Item;

@XmlAccessorType(XmlAccessType.FIELD)
public class SoapItem {
    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String productCode;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String name;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String unitCode;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String unitName;

    public static SoapItem from(Item item) {
        if (item == null) {
            return null;
        }

        SoapItem result = new SoapItem();
        result.productCode = item.getProductCode();
        result.name = item.getName();
        result.unitCode = item.getUnitCode();
        result.unitName = item.getUnitName();
        return result;
    }
}
