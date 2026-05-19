package ru.waybill.helpers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import ru.waybill.document.Item;
import ru.waybill.document.Organization;
import ru.waybill.document.WaybillDocument;
import ru.waybill.document.WaybillDocumentLine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class WaybillExampleWorkbookWriter {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: WaybillExampleWorkbookWriter <templatePath> <outputPath>");
        }

        Path templatePath = Path.of(args[0]);
        Path outputPath = Path.of(args[1]);

        WaybillDocument document = createExampleDocument();

        try (InputStream input = Files.newInputStream(templatePath);
             Workbook workbook = WorkbookFactory.create(input)) {
            fillWorkbook(workbook, document);

            try (OutputStream output = Files.newOutputStream(outputPath)) {
                workbook.write(output);
            }
        }
    }

    private static WaybillDocument createExampleDocument() {
        WaybillDocument document = new WaybillDocument();
        document.setInvoiceNumber("УПД-000456");
        document.setInvoiceDate(LocalDate.of(2026, 5, 19));
        document.setStatus(1);
        document.setSeller(new Organization("АО \"Контур-Снаб\"", "7723456789 / 772301001"));
        document.setBuyer(new Organization("ООО \"Альфа-Маркет\"", "7804567890 / 780401001"));
        document.setCurrencyName("Российский рубль");
        document.setCurrencyCode("643");
        document.setTransferBasis("Договор поставки № 18/26 от 12.05.2026");

        WaybillDocumentLine firstLine = createLine(
                1,
                "SKU-1001",
                "Монитор 27 дюймов",
                "796",
                "шт",
                new BigDecimal("3"),
                new BigDecimal("18500"),
                new BigDecimal("55500"),
                new BigDecimal("11100"),
                new BigDecimal("66600")
        );
        WaybillDocumentLine secondLine = createLine(
                2,
                "SKU-2040",
                "Клавиатура проводная",
                "796",
                "шт",
                new BigDecimal("10"),
                new BigDecimal("1200"),
                new BigDecimal("12000"),
                new BigDecimal("2400"),
                new BigDecimal("14400")
        );
        List<WaybillDocumentLine> lines = List.of(firstLine, secondLine);

        document.setLines(lines);
        return document;
    }

    private static WaybillDocumentLine createLine(
            int lineNumber,
            String productCode,
            String itemName,
            String unitCode,
            String unitName,
            BigDecimal quantity,
            BigDecimal unitPrice,
            BigDecimal amountWithoutTax,
            BigDecimal taxAmount,
            BigDecimal amountWithTax
    ) {
        WaybillDocumentLine line = new WaybillDocumentLine();
        line.setLineNumber(lineNumber);
        line.setItem(new Item(productCode, itemName, unitCode, unitName));
        line.setQuantity(quantity);
        line.setUnitPrice(unitPrice);
        line.setAmountWithoutTax(amountWithoutTax);
        line.setTaxAmount(taxAmount);
        line.setAmountWithTax(amountWithTax);
        return line;
    }

    private static void fillWorkbook(Workbook workbook, WaybillDocument document) {
        Sheet sheet = workbook.getSheetAt(0);

        setCellValue(sheet, "AM1", document.getInvoiceNumber());
        setCellValue(sheet, "BB1", document.getInvoiceDate().toString());
        setCellValue(sheet, "P6", document.getStatus());
        setCellValue(sheet, "AM4", document.getSeller().getName());
        setCellValue(sheet, "AM6", document.getSeller().getInnKpp());
        setCellValue(sheet, "AM10", document.getBuyer().getName());
        setCellValue(sheet, "AM12", document.getBuyer().getInnKpp());
        setCellValue(sheet, "AM13", document.getCurrencyName() + ", " + document.getCurrencyCode());
        setCellValue(sheet, "B32", document.getTransferBasis());

        int rowIndex = 18;
        for (WaybillDocumentLine line : document.getLines()) {
            fillLine(sheet, rowIndex, line);
            rowIndex++;
        }

        setCellValue(sheet, "CF23", sum(document.getLines(), AmountType.WITHOUT_TAX));
        setCellValue(sheet, "DI23", sum(document.getLines(), AmountType.TAX));
        setCellValue(sheet, "DV23", sum(document.getLines(), AmountType.WITH_TAX));
    }

    private static void fillLine(Sheet sheet, int rowNumber, WaybillDocumentLine line) {
        Item item = line.getItem();
        setCellValue(sheet, "A" + rowNumber, line.getLineNumber());
        setCellValue(sheet, "G" + rowNumber, item.getProductCode());
        setCellValue(sheet, "T" + rowNumber, item.getName());
        setCellValue(sheet, "AW" + rowNumber, item.getUnitCode());
        setCellValue(sheet, "BC" + rowNumber, item.getUnitName());
        setCellValue(sheet, "BL" + rowNumber, line.getQuantity());
        setCellValue(sheet, "BU" + rowNumber, line.getUnitPrice());
        setCellValue(sheet, "CF" + rowNumber, line.getAmountWithoutTax());
        setCellValue(sheet, "DI" + rowNumber, line.getTaxAmount());
        setCellValue(sheet, "DV" + rowNumber, line.getAmountWithTax());
    }

    private static BigDecimal sum(List<WaybillDocumentLine> lines, AmountType amountType) {
        return lines.stream()
                .map(line -> switch (amountType) {
                    case WITHOUT_TAX -> line.getAmountWithoutTax();
                    case TAX -> line.getTaxAmount();
                    case WITH_TAX -> line.getAmountWithTax();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static void setCellValue(Sheet sheet, String address, String value) {
        cell(sheet, address).setCellValue(value);
    }

    private static void setCellValue(Sheet sheet, String address, Integer value) {
        cell(sheet, address).setCellValue(value);
    }

    private static void setCellValue(Sheet sheet, String address, BigDecimal value) {
        cell(sheet, address).setCellValue(value.doubleValue());
    }

    private static Cell cell(Sheet sheet, String address) {
        CellReference reference = new CellReference(address);
        Row row = sheet.getRow(reference.getRow());
        if (row == null) {
            row = sheet.createRow(reference.getRow());
        }
        Cell cell = row.getCell(reference.getCol());
        if (cell == null) {
            cell = row.createCell(reference.getCol());
        }
        return cell;
    }

    private enum AmountType {
        WITHOUT_TAX,
        TAX,
        WITH_TAX
    }
}
