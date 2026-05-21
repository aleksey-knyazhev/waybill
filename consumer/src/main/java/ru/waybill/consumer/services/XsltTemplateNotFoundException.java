package ru.waybill.consumer.services;

public class XsltTemplateNotFoundException extends RuntimeException {
    public XsltTemplateNotFoundException(String fileName) {
        super("XSLT template not found: " + fileName);
    }
}
