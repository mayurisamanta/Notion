package com.example.notion.repository;

import com.example.notion.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo, Integer> {

    Optional<UserInfo> findByEmailId(String emailId);
}
