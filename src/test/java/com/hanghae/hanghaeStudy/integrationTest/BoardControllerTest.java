package com.hanghae.hanghaeStudy.integrationTest;

import com.hanghae.hanghaeStudy.entity.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
