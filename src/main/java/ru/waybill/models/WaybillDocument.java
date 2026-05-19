package ru.waybill.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WaybillDocument {
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private Integer status;
    private Organization seller;
    private Organization buyer;
    private String currencyName;
    private String currencyCode;
    private String transferBasis;
    private List<WaybillDocumentLine> lines = new ArrayList<>();

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Organization getSeller() {
        return seller;
    }

    public void setSeller(Organization seller) {
        this.seller = seller;
    }

    public Organization getBuyer() {
        return buyer;
    }

    public void setBuyer(Organization buyer) {
        this.buyer = buyer;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTransferBasis() {
        return transferBasis;
    }

    public void setTransferBasis(String transferBasis) {
        this.transferBasis = transferBasis;
    }

    public List<WaybillDocumentLine> getLines() {
        return lines;
    }

    public void setLines(List<WaybillDocumentLine> lines) {
        this.lines = lines;
    }
}
