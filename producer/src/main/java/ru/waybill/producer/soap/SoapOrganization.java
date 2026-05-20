package ru.waybill.producer.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoapOrganization {
    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String name;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String innKpp;
}
