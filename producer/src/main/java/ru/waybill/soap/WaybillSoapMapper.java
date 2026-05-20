package ru.waybill.soap;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.waybill.models.Item;
import ru.waybill.models.Organization;
import ru.waybill.models.WaybillDocument;
import ru.waybill.models.WaybillDocumentLine;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface WaybillSoapMapper {
    @Mapping(target = "waybillDocument", source = "document")
    GetWaybillDocumentResponse toResponse(WaybillDocument document);

    SoapWaybillDocument toSoapDocument(WaybillDocument document);

    SoapWaybillDocumentLine toSoapLine(WaybillDocumentLine line);

    SoapOrganization toSoapOrganization(Organization organization);

    SoapItem toSoapItem(Item item);

    default String map(LocalDate value) {
        return value == null ? null : value.toString();
    }
}
