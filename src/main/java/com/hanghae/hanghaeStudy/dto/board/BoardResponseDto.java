package com.hanghae.hanghaeStudy.dto.board;

import com.hanghae.hanghaeStudy.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String userName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAT;

    public static BoardResponseDto toDto(Board board){
        return new BoardResponseDto(
            board.getId(),
            board.getUser().getUsername(),
            board.getTitle(),
            board.getContent(),
            board.getCreatedAt(),
            board.getUpdatedAt()
        );
    }

}
