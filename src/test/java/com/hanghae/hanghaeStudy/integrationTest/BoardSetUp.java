package com.hanghae.hanghaeStudy.integrationTest;

import com.hanghae.hanghaeStudy.entity.Board;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BoardSetUp {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Long saveBoard(String title, String content, User user){
        Board board = Board.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
        return boardRepository.save(board).getId();
    }
}
