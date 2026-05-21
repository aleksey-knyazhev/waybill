package ru.waybill.consumer.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.waybill.consumer.models.OrganizationHeader;
import ru.waybill.consumer.models.WaybillDocumentHeader;
import ru.waybill.consumer.services.WaybillDocumentHeaderClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class WaybillDocumentsController {
    private final WaybillDocumentHeaderClient documentHeaderClient;
    private final String waybillDocumentsPage;
    private final String waybillDocumentsTable;
    private final String waybillDocumentsEmpty;

    public WaybillDocumentsController(WaybillDocumentHeaderClient documentHeaderClient) throws IOException {
        this.documentHeaderClient = documentHeaderClient;
        this.waybillDocumentsPage = loadWaybillDocumentsPage();
        this.waybillDocumentsTable = loadResource("frontend/waybill-documents-table.html");
        this.waybillDocumentsEmpty = loadResource("frontend/waybill-documents-empty.html");
    }

    @GetMapping(value = "/list/waybill-documents", produces = MediaType.TEXT_HTML_VALUE)
    public String getWaybillDocuments() {
        return page(documentHeaderClient.getDocumentsHeaders());
    }

    private String page(List<WaybillDocumentHeader> documents) {
        StringBuilder rows = new StringBuilder();
        for (WaybillDocumentHeader document : documents) {
            rows.append("<tr>")
                    .append(cell(document.id()))
                    .append(cell(document.invoiceNumber()))
                    .append(cell(document.invoiceDate()))
                    .append(cell(document.status()))
                    .append(cell(organizationName(document.seller())))
                    .append(cell(organizationInnKpp(document.seller())))
                    .append(cell(organizationName(document.buyer())))
                    .append(cell(organizationInnKpp(document.buyer())))
                    .append(cell(currency(document)))
                    .append(cell(document.transferBasis()))
                    .append("</tr>");
        }

        return waybillDocumentsPage.replace("${documentsContent}", tableOrEmpty(rows, documents));
    }

    private String tableOrEmpty(StringBuilder rows, List<WaybillDocumentHeader> documents) {
        if (documents.isEmpty()) {
            return waybillDocumentsEmpty;
        }
        return waybillDocumentsTable.replace("${rows}", rows);
    }

    private String cell(Object value) {
        return "<td>" + escape(value == null ? "" : value.toString()) + "</td>";
    }

    private String organizationName(OrganizationHeader organization) {
        return organization == null ? "" : organization.name();
    }

    private String organizationInnKpp(OrganizationHeader organization) {
        return organization == null ? "" : organization.innKpp();
    }

    private String currency(WaybillDocumentHeader document) {
        String name = document.currencyName() == null ? "" : document.currencyName();
        String code = document.currencyCode() == null ? "" : document.currencyCode();
        return (name + " " + code).trim();
    }

    private String escape(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String loadWaybillDocumentsPage() throws IOException {
        return loadResource("frontend/waybill-documents.html");
    }

    private String loadResource(String path) throws IOException {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
