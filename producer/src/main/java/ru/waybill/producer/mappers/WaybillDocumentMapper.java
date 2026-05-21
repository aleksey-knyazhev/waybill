package ru.waybill.producer.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.waybill.producer.models.WaybillDocument;
import ru.waybill.producer.models.WaybillDocumentHeader;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WaybillDocumentMapper {
    WaybillDocumentHeader toHeader(WaybillDocument document);

    List<WaybillDocumentHeader> toHeaders(List<WaybillDocument> documents);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoiceNumber", ignore = true)
    @Mapping(target = "invoiceDate", ignore = true)
    void updateDocument(@MappingTarget WaybillDocument target, WaybillDocument source);
}
