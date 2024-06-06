package com.AutoMeet.domain.user.repository;

import com.AutoMeet.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT u.name FROM User u WHERE u.id = :id")
    String findNameById(@Param("id") Long id);
}