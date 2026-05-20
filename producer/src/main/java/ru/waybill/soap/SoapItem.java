package ru.waybill.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.waybill.models.Item;

@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@AllArgsConstructor
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

        return new SoapItem(
                item.getProductCode(),
                item.getName(),
                item.getUnitCode(),
                item.getUnitName()
        );
    }
}
