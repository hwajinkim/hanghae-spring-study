package com.hanghae.hanghaeStudy.integrationTest;

import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSetUp {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Long saveUser(String userName, String password, List<String> roles){
        User user = User.builder()
                .username(userName)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();
        return userRepository.save(user).getId();
    }
}
