package ru.waybill.producer.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
public class WaybillWsdlController {
    private final String wsdlTemplate;
    private final String xsd;

    public WaybillWsdlController() throws IOException {
        this.wsdlTemplate = loadResource("wsdl/waybill.wsdl");
        this.xsd = loadResource("xsd/waybill.xsd");
    }

    @GetMapping(value = "/soap/waybill.wsdl", produces = MediaType.TEXT_XML_VALUE)
    public String getWsdl(HttpServletRequest request) {
        return wsdl(request);
    }

    @GetMapping(value = "/soap/waybill", params = "wsdl", produces = MediaType.TEXT_XML_VALUE)
    public String getWsdlByQuery(HttpServletRequest request) {
        return wsdl(request);
    }

    @GetMapping(value = "/soap/waybill.xsd", produces = MediaType.APPLICATION_XML_VALUE)
    public String getXsd() {
        return xsd;
    }

    private String wsdl(HttpServletRequest request) {
        return wsdlTemplate
                .replace("${serviceLocation}", baseUrl(request) + "/soap/waybill")
                .replace("${xsdLocation}", baseUrl(request) + "/soap/waybill.xsd");
    }

    private String baseUrl(HttpServletRequest request) {
        int port = request.getServerPort();
        boolean defaultPort = ("http".equals(request.getScheme()) && port == 80)
                || ("https".equals(request.getScheme()) && port == 443);
        String portPart = defaultPort ? "" : ":" + port;
        return request.getScheme() + "://" + request.getServerName() + portPart;
    }

    private String loadResource(String path) throws IOException {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
