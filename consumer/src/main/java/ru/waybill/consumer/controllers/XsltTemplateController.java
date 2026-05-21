package ru.waybill.consumer.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import ru.waybill.consumer.models.XsltTemplate;
import ru.waybill.consumer.services.XsltTemplateNotFoundException;
import ru.waybill.consumer.services.XsltTemplateService;
import ru.waybill.consumer.services.XsltTemplateService.XsltTemplateOption;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SEE_OTHER;

@RestController
public class XsltTemplateController {
    private final XsltTemplateService xsltTemplateService;

    public XsltTemplateController(XsltTemplateService xsltTemplateService) {
        this.xsltTemplateService = xsltTemplateService;
    }

    @GetMapping(value = "/xslt", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<XsltTemplateOption> getTemplates() {
        return xsltTemplateService.getOptions();
    }

    @GetMapping(value = "/xslt/{fileName:.+}")
    public ResponseEntity<String> getTemplate(@PathVariable String fileName) {
        try {
            XsltTemplate template = xsltTemplateService.getByFileName(fileName);
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.noStore())
                    .contentType(MediaType.parseMediaType(template.getContentType()))
                    .body(template.getContent());
        } catch (XsltTemplateNotFoundException exception) {
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PostMapping(value = "/xslt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadTemplate(@RequestParam("file") MultipartFile file) {
        try {
            XsltTemplate template = xsltTemplateService.save(file);
            return ResponseEntity
                    .status(SEE_OTHER)
                    .location(URI.create("/import/waybill-document?xslt=" + template.getVersion()))
                    .build();
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(BAD_REQUEST, exception.getMessage(), exception);
        } catch (IOException exception) {
            throw new ResponseStatusException(BAD_REQUEST, "Cannot read XSLT file", exception);
        }
    }
}
