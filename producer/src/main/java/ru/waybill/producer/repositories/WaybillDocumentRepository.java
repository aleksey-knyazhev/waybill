package ru.waybill.producer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import ru.waybill.producer.models.WaybillDocument;

import java.util.Optional;

public interface WaybillDocumentRepository extends JpaRepository<WaybillDocument, Long> {
    @EntityGraph(attributePaths = "lines")
    Optional<WaybillDocument> findTopByOrderByIdDesc();
}
