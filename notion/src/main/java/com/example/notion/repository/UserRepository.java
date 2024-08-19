package com.example.notion.repository;

import com.example.notion.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository for handling user related operations
 */
public interface UserRepository extends JpaRepository<UserInfo, Integer> {

    /**
     * Find user by email id
     *
     * @param emailId email id
     * @return user info
     */
    Optional<UserInfo> findByEmailId(String emailId);
}
