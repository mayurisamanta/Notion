package com.example.notion.repository;

import com.example.notion.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * UserRepository for handling user related operations
 */
public interface UserRepository extends JpaRepository<UserInfo, Integer> {

    /**
     * Find user by email id and xStatus
     *
     * @param emailId email id
     * @return user info
     */
    Optional<UserInfo> findByEmailIdAndStatus(String emailId, Byte xStatus);

    /**
     * Update last login at
     *
     * @param emailId email id
     */
    @Modifying
    @Query("UPDATE UserInfo u " +
            "SET u.lastLoginAt = CURRENT_TIMESTAMP, u.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE u.emailId = :emailId AND u.status = 1")
    void updateLastLoginAt(String emailId);

    /**
     * Get user info by user id
     *
     * @param userId user id
     * @return user info
     */
     @Query(value = "SELECT u.userid as userId, u.username as username, u.emailid as emailId, If(u.xstatus = 1, 'Active', 'InActive') as status, " +
             " DATE_FORMAT(u.lastloginat, '%Y-%m-%d %H:%i:%s') as lastLoginAt, DATE_FORMAT(u.createdat, '%Y-%m-%d %H:%i:%s') as createdAt, " +
             "DATE_FORMAT(u.updatedat, '%Y-%m-%d %H:%i:%s') as updatedAt " +
             "FROM user u " +
             "WHERE u.userId = :userId", nativeQuery = true)
     Map<String, Object> getUserInfoById(Integer userId);

    /**
     * Get all user info
     *
     * @return user info
     */
    @Query(value = "SELECT u.userid as userId, u.username as username, u.emailid as emailId, If(u.xstatus = 1, 'Active', 'InActive') as status, " +
            " DATE_FORMAT(u.lastloginat, '%Y-%m-%d %H:%i:%s') as lastLoginAt, DATE_FORMAT(u.createdat, '%Y-%m-%d %H:%i:%s') as createdAt, " +
            "DATE_FORMAT(u.updatedat, '%Y-%m-%d %H:%i:%s') as updatedAt " +
            "FROM user u ", nativeQuery = true)
    List<Map<String, Object>> getAllUsers();

    /**
     * Delete user by user id
     *
     * @param userId user id
     */
    @Modifying
    @Query("UPDATE UserInfo u " +
            "SET u.status = 0, u.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE u.userId = :userId")
    void deleteByUserId(Integer userId);
}
