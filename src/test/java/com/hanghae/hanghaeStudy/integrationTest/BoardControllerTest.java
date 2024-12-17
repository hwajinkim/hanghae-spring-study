package com.hanghae.hanghaeStudy.integrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.hanghaeStudy.dto.board.BoardDeleteDto;
import com.hanghae.hanghaeStudy.dto.board.BoardRequestDto;
import com.hanghae.hanghaeStudy.dto.board.BoardUpdateDto;
import com.hanghae.hanghaeStudy.entity.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardControllerTest extends BaseIntegrationTest{

    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private BoardSetUp boardSetUp;

    @Test
    @DisplayName("전체 게시글 조회 테스트")
    public void getBoardsTest() throws Exception {
        //given
        String username="hwajin00";
        String password="hwajin1234";
        User user = userSetUp.saveUser(username, password, List.of("USER"));

        String title = "title 1";
        String content = "content 1";
        long boardId = boardSetUp.saveBoard(title, content, user);

        //when
        ResultActions resultActions = mvc.perform(get("/api/boards")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("전체 게시물 조회에 성공했습니다.")))
                .andExpect(jsonPath("data", Matchers.is(notNullValue())));
    }

    @Test
    @DisplayName("특정 게시글 조회 테스트")
    public void getBoardByIdTest() throws Exception {
        //given
        String username="hwajin00";
        String password="hwajin1234";
        User user = userSetUp.saveUser(username, password, List.of("USER"));

        String title = "title 1";
        String content = "content 1";

        long boardId = boardSetUp.saveBoard(title, content, user);

        //when
        ResultActions resultActions = mvc.perform(get("/api/boardDetail/"+boardId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("게시물 조회에 성공했습니다.")))
                .andExpect(jsonPath("data", Matchers.is(notNullValue())));
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    @WithMockUser(username = "hwajin00")
    public void boardPostTest() throws Exception {
        //given
        String username="hwajin00";
        String password="hwajin1234";
        User user = userSetUp.saveUser(username, password, List.of("USER"));

        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setTitle("title 1");
        boardRequestDto.setContent("content 1");

        //when
        ResultActions resultActions = mvc.perform(post("/api/board")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardRequestDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("게시물 등록에 성공했습니다.")))
                .andExpect(jsonPath("data", Matchers.is(notNullValue())));
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    @WithMockUser(username = "hwajin00")
    public void boardPutTest() throws Exception {
        //given
        String username="hwajin00";
        String password="hwajin1234";
        User user = userSetUp.saveUser(username, password, List.of("USER"));

        String title = "title 1";
        String content = "content 1";
        long boardId = boardSetUp.saveBoard(title, content, user);

        BoardUpdateDto boardUpdateDto = new BoardUpdateDto("title 2", "content 2", "hwajin1234");
        //when
        ResultActions resultActions = mvc.perform(put("/api/board/"+boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardUpdateDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("게시물 수정에 성공했습니다.")))
                .andExpect(jsonPath("data", Matchers.is(notNullValue())));
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    @WithMockUser(username = "hwajin00")
    public void boardDeleteTest() throws Exception {
        //given
        String username="hwajin00";
        String password="hwajin1234";
        User user = userSetUp.saveUser(username, password, List.of("USER"));

        String title = "title 1";
        String content = "content 1";
        long boardId = boardSetUp.saveBoard(title, content, user);

        BoardDeleteDto boardDeleteDto = new BoardDeleteDto("hwajin1234");
        //when
        ResultActions resultActions = mvc.perform(delete("/api/board/"+boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDeleteDto))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print());
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is("true")))
                .andExpect(jsonPath("message", Matchers.is("게시물 삭제에 성공했습니다.")));
    }
}
