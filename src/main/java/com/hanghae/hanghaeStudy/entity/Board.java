package com.hanghae.hanghaeStudy.entity;

import com.hanghae.hanghaeStudy.dto.board.BoardRequestDto;
import com.hanghae.hanghaeStudy.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder
    public Board(User user, String title, String content){
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public static Board toEntity(BoardRequestDto boardRequestDto){
        return new Board(boardRequestDto.getUser(), boardRequestDto.getTitle(), boardRequestDto.getContent());
    }

    public void updateBoard(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
    }
}
