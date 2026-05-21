package ru.waybill.producer.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.waybill.producer.models.Item;
import ru.waybill.producer.models.Organization;
import ru.waybill.producer.models.WaybillDocument;
import ru.waybill.producer.models.WaybillDocumentLine;
import ru.waybill.producer.soap.GetWaybillDocumentResponse;
import ru.waybill.producer.soap.SoapItem;
import ru.waybill.producer.soap.SoapOrganization;
import ru.waybill.producer.soap.SoapWaybillDocument;
import ru.waybill.producer.soap.SoapWaybillDocumentLine;

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
