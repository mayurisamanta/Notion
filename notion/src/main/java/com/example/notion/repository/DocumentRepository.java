package com.example.notion.repository;

import com.example.notion.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {


    /**
     * Find document by id and status
     *
     * @param documentId
     * @return
     */
    Optional<Document> findByDocumentIdAndStatus(Integer documentId, Byte status);

    /**
     * Find all documents by parent and status
     *
     * @param document
     * @param status
     * @return
     */
    List<Document> findByParentAndStatus(Document document, Byte status);
}
