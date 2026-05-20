package ru.waybill.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.waybill.models.Organization;

@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@AllArgsConstructor
public class SoapOrganization {
    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String name;

    @XmlElement(namespace = SoapNamespaces.WAYBILL)
    private String innKpp;

    public static SoapOrganization from(Organization organization) {
        if (organization == null) {
            return null;
        }

        return new SoapOrganization(organization.getName(), organization.getInnKpp());
    }
}
