package com.hanghae.hanghaeStudy.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateDto {

    private String title;
    private String content;
    private String password;

}
