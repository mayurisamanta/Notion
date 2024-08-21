package com.example.notion.repository;

import com.example.notion.entity.DocumentUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, Integer> {


    Optional<DocumentUpload> findByDocumentUploadIdAndStatus(Integer documentUploadId, Byte status);
}
