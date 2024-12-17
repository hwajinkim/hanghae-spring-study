package com.hanghae.hanghaeStudy.integrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.hanghaeStudy.dto.user.UserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends BaseIntegrationTest{

    @Test
    @DisplayName("회원가입 테스트")
    public void signupTest() throws Exception {
        //given
        String username = "hwajin00";
        String password = "hwajin1234";
        UserRequestDto userRequestDto = new UserRequestDto(username, password, List.of("USER"));

        //when
        ResultActions resultActions = mvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is("true")))
                .andExpect(jsonPath("message", is("사용자 생성 성공")));
    }
}
