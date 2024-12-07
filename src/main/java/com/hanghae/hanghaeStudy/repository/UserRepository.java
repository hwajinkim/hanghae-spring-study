package com.hanghae.hanghaeStudy.repository;

import com.hanghae.hanghaeStudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);

    boolean existsByUsername(String username);
}
