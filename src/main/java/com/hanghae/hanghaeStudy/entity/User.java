package com.hanghae.hanghaeStudy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", unique = true, nullable = false)
    private Long id;

    @Column(length = 10, unique = true, nullable = false)
    private String username;

    @Column(length = 15, nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @Builder
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

}
