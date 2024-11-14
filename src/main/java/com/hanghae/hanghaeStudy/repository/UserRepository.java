package com.hanghae.hanghaeStudy.repository;

import com.hanghae.hanghaeStudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String userName);
}
