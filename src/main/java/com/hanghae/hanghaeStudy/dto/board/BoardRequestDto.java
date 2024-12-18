package com.hanghae.hanghaeStudy.dto.board;


import com.hanghae.hanghaeStudy.entity.Board;
import com.hanghae.hanghaeStudy.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDto {

    private String title;
    private String content;
    private User user;

    public static BoardRequestDto toDto(Board board){
        return new BoardRequestDto(
                board.getTitle(),
                board.getContent(),
                board.getUser()
        );
    }

}
