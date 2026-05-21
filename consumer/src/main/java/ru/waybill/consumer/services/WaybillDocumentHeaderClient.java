package ru.waybill.consumer.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.waybill.consumer.models.WaybillDocumentHeader;

import java.util.List;

@Service
public class WaybillDocumentHeaderClient {
    private final RestClient restClient;
    private final String documentsHeadersUrl;

    public WaybillDocumentHeaderClient(
            @Value("${producer.documents-headers-url}") String documentsHeadersUrl
    ) {
        this.restClient = RestClient.builder().build();
        this.documentsHeadersUrl = documentsHeadersUrl;
    }

    public List<WaybillDocumentHeader> getDocumentsHeaders() {
        return restClient.get()
                .uri(documentsHeadersUrl)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
