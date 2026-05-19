package ru.waybill.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import ru.waybill.models.Organization;

@XmlAccessorType(XmlAccessType.FIELD)
public class SoapOrganization {
    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String name;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String innKpp;

    public static SoapOrganization from(Organization organization) {
        if (organization == null) {
            return null;
        }

        SoapOrganization result = new SoapOrganization();
        result.name = organization.getName();
        result.innKpp = organization.getInnKpp();
        return result;
    }
}
