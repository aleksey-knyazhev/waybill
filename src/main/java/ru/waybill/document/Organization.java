package ru.waybill.document;

public class Organization {
    private String name;
    private String innKpp;

    public Organization() {
    }

    public Organization(String name, String innKpp) {
        this.name = name;
        this.innKpp = innKpp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInnKpp() {
        return innKpp;
    }

    public void setInnKpp(String innKpp) {
        this.innKpp = innKpp;
    }
}
