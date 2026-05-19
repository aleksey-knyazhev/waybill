package ru.waybill.helpers;

import ru.waybill.models.Item;
import ru.waybill.models.Organization;
import ru.waybill.models.WaybillDocument;
import ru.waybill.models.WaybillDocumentLine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class WaybillDocumentExamples {
    private WaybillDocumentExamples() {
    }

    public static WaybillDocument createExampleDocument() {
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

        document.setLines(List.of(firstLine, secondLine));
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
}
