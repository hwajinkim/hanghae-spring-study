package com.hanghae.hanghaeStudy.integrationTest;

import com.hanghae.hanghaeStudy.dto.auth.SignInRequestDto;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends BaseIntegrationTest{

    @Autowired
    private UserSetUp userSetUp;
    @Test
    @DisplayName("로그인 테스트")
    public void signinTest() throws Exception{

        //given
        String username="hwajin00";
        String password="hwajin1234";
        User user = userSetUp.saveUser(username, password, List.of("USER"));

        SignInRequestDto signInRequestDto = new SignInRequestDto(username, password);

        //when
        ResultActions resultActions = mvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequestDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is("true")))
                .andExpect(jsonPath("message", is("사용자 로그인 성공")))
                .andExpect(jsonPath("httpHeaders", is(notNullValue())));


    }
}

