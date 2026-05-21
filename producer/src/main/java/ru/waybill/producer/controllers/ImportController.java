package ru.waybill.producer.controllers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import ru.waybill.producer.models.Item;
import ru.waybill.producer.models.Organization;
import ru.waybill.producer.models.WaybillDocument;
import ru.waybill.producer.models.WaybillDocumentLine;
import ru.waybill.producer.services.WaybillDocumentService;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
public class ImportController {
    private static final int FIRST_LINE_ROW = 18;
    private static final int LAST_LINE_ROW = 22;
    private static final DataFormatter FORMATTER = new DataFormatter(Locale.forLanguageTag("ru-RU"));
    private final WaybillDocumentService documentService;

    public ImportController(WaybillDocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/api/import/waybill", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public WaybillDocument importWaybill(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase(Locale.ROOT).endsWith(".xls")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only .xls files are supported");
        }
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        try (InputStream input = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(input)) {
            WaybillDocument document = readDocument(workbook.getSheetAt(0));
            return documentService.setDocument(document);
        }
    }

    private WaybillDocument readDocument(Sheet sheet) {
        WaybillDocument document = new WaybillDocument();
        document.setInvoiceNumber(text(sheet, "AM1"));
        document.setInvoiceDate(date(sheet, "BB1"));
        document.setStatus(integer(sheet, "P6"));
        document.setSeller(new Organization(text(sheet, "AM4"), text(sheet, "AM6")));
        document.setBuyer(new Organization(text(sheet, "AM10"), text(sheet, "AM12")));
        setCurrency(document, text(sheet, "AM13"));
        document.setTransferBasis(text(sheet, "B32"));
        document.setLines(readLines(sheet));
        return document;
    }

    private List<WaybillDocumentLine> readLines(Sheet sheet) {
        List<WaybillDocumentLine> lines = new ArrayList<>();
        for (int rowNumber = FIRST_LINE_ROW; rowNumber <= LAST_LINE_ROW; rowNumber++) {
            WaybillDocumentLine line = readLine(sheet, rowNumber);
            if (line != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private WaybillDocumentLine readLine(Sheet sheet, int rowNumber) {
        String productCode = text(sheet, "G" + rowNumber);
        String itemName = text(sheet, "T" + rowNumber);
        if (productCode.isBlank() && itemName.isBlank()) {
            return null;
        }

        WaybillDocumentLine line = new WaybillDocumentLine();
        line.setLineNumber(integer(sheet, "A" + rowNumber));
        line.setItem(new Item(
                productCode,
                itemName,
                text(sheet, "AW" + rowNumber),
                text(sheet, "BC" + rowNumber)
        ));
        line.setQuantity(decimal(sheet, "BL" + rowNumber));
        line.setUnitPrice(decimal(sheet, "BU" + rowNumber));
        line.setAmountWithoutTax(decimal(sheet, "CF" + rowNumber));
        line.setTaxAmount(decimal(sheet, "DI" + rowNumber));
        line.setAmountWithTax(decimal(sheet, "DV" + rowNumber));
        return line;
    }

    private void setCurrency(WaybillDocument document, String currency) {
        int separatorIndex = currency.lastIndexOf(',');
        if (separatorIndex < 0) {
            document.setCurrencyName(currency);
            document.setCurrencyCode("");
            return;
        }

        document.setCurrencyName(currency.substring(0, separatorIndex).trim());
        document.setCurrencyCode(currency.substring(separatorIndex + 1).trim());
    }

    private String text(Sheet sheet, String address) {
        Cell cell = cell(sheet, address);
        if (cell == null) {
            return "";
        }
        return FORMATTER.formatCellValue(cell).trim();
    }

    private Integer integer(Sheet sheet, String address) {
        BigDecimal value = decimal(sheet, address);
        if (value == null) {
            return null;
        }
        return value.intValue();
    }

    private BigDecimal decimal(Sheet sheet, String address) {
        Cell cell = cell(sheet, address);
        if (cell == null) {
            return null;
        }
        return switch (cell.getCellType()) {
            case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING -> decimal(cell.getStringCellValue());
            case FORMULA -> BigDecimal.valueOf(cell.getNumericCellValue());
            default -> null;
        };
    }

    private BigDecimal decimal(String value) {
        String normalized = value
                .replace("\u00a0", "")
                .replace(" ", "")
                .replace(",", ".")
                .trim();
        if (normalized.isBlank()) {
            return null;
        }
        return new BigDecimal(normalized);
    }

    private LocalDate date(Sheet sheet, String address) {
        Cell cell = cell(sheet, address);
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        String value = text(sheet, address);
        if (value.isBlank()) {
            return null;
        }
        for (DateTimeFormatter formatter : List.of(DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ofPattern("dd.MM.yyyy"))) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (RuntimeException ignored) {
                // Try the next supported date format.
            }
        }
        throw new IllegalArgumentException("Unsupported date format: " + value);
    }

    private Cell cell(Sheet sheet, String address) {
        CellReference reference = new CellReference(address);
        Row row = sheet.getRow(reference.getRow());
        if (row == null) {
            return null;
        }
        return row.getCell(reference.getCol());
    }
}
