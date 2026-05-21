package ru.waybill.consumer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.waybill.consumer.models.XsltTemplate;

import java.util.Optional;
import java.util.List;

public interface XsltTemplateRepository extends JpaRepository<XsltTemplate, Long> {
    Optional<XsltTemplate> findByVersion(String version);

    Optional<XsltTemplate> findByFileName(String fileName);

    List<XsltTemplate> findAllByOrderByVersionAsc();
}
