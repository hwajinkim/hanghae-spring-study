package com.hanghae.hanghaeStudy.controller;

import com.hanghae.hanghaeStudy.dto.user.UserRequestDto;
import com.hanghae.hanghaeStudy.dto.user.UserResponseDto;
import com.hanghae.hanghaeStudy.response.ApiResponse;
import com.hanghae.hanghaeStudy.response.ResponseCode;
import com.hanghae.hanghaeStudy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<UserResponseDto> signup(@RequestBody UserRequestDto userRequestDto){
        UserResponseDto userResponseDto = userService.signup(userRequestDto);
        return ApiResponse.success(ResponseCode.USER_CREATE_SUCCESS.getMessage(), null);
    }

    @PostMapping("/signin")
    public ApiResponse<UserResponseDto> signIn(@RequestBody UserRequestDto userRequestDto){
        UserResponseDto userResponseDto = userService.signIn(userRequestDto);
        return ApiResponse.success(ResponseCode.USER_LOGIN_SUCCESS.getMessage(), null);
    }
}
