package ru.waybill.consumer.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "xslt_template", schema = "waybill")
public class XsltTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 2)
    private String version;

    @Column(nullable = false, unique = true, length = 128)
    private String fileName;

    @Column(nullable = false, length = 128)
    private String contentType;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    public XsltTemplate() {
    }

    public XsltTemplate(String version, String fileName, String contentType, String content) {
        this.version = version;
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
    }

    public void update(String fileName, String contentType, String content) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }
}
