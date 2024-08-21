package com.example.notion.repository;

import com.example.notion.entity.Document;
import com.example.notion.entity.DocumentHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Integer> {

    /**
     * Get the latest version of the document
     * @param document
     */
    @Query("SELECT d.version FROM DocumentHistory d WHERE d.document = :document ORDER BY d.version DESC LIMIT 1")
    Optional<Integer> findLastVersionByDocument(Document document);

    /**
     * Get the document history by document limited by pageable
     * @param document
     * @param pageable
     */
    List<DocumentHistory> findByDocumentOrderByVersionDesc(Document document, Pageable pageable);
}
