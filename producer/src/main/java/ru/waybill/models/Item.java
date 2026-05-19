package ru.waybill.models;

public class Item {
    private String productCode;
    private String name;
    private String unitCode;
    private String unitName;

    public Item() {
    }

    public Item(String productCode, String name, String unitCode, String unitName) {
        this.productCode = productCode;
        this.name = name;
        this.unitCode = unitCode;
        this.unitName = unitName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
