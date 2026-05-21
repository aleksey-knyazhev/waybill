package ru.waybill.consumer.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.waybill.consumer.models.XsltTemplate;
import ru.waybill.consumer.repositories.XsltTemplateRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class XsltTemplateService {
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("waybill-document_version_(\\d{2})\\.xsl");
    private static final String CONTENT_TYPE = "application/xslt+xml";

    private final XsltTemplateRepository repository;

    public XsltTemplateService(XsltTemplateRepository repository) {
        this.repository = repository;
    }

    public XsltTemplate getByFileName(String fileName) {
        return repository.findByFileName(fileName)
                .orElseThrow(() -> new XsltTemplateNotFoundException(fileName));
    }

    public List<XsltTemplateOption> getOptions() {
        return repository.findAllByOrderByVersionAsc().stream()
                .map(template -> new XsltTemplateOption(template.getVersion(), template.getFileName()))
                .toList();
    }

    @Transactional
    public XsltTemplate save(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("XSLT file is required");
        }

        String fileName = cleanFileName(file.getOriginalFilename());
        String version = version(fileName);
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);

        XsltTemplate template = repository.findByVersion(version)
                .orElseGet(() -> new XsltTemplate(version, fileName, CONTENT_TYPE, content));
        template.update(fileName, CONTENT_TYPE, content);
        return repository.save(template);
    }

    private String cleanFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("XSLT file name is required");
        }
        return fileName.replace("\\", "/").substring(fileName.replace("\\", "/").lastIndexOf('/') + 1);
    }

    private String version(String fileName) {
        Matcher matcher = FILE_NAME_PATTERN.matcher(fileName);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("File name must match waybill-document_version_XX.xsl");
        }
        return matcher.group(1);
    }

    public record XsltTemplateOption(String version, String fileName) {
    }
}
