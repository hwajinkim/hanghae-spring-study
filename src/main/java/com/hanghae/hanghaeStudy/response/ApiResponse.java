package com.hanghae.hanghaeStudy.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hanghae.hanghaeStudy.dto.auth.TokenDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

@JsonInclude(JsonInclude.Include.NON_NULL) // null 노출 x
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 접근 레벨 private
@Getter
public class ApiResponse<T> {

    private int statusCode;
    private String success;
    private String message;
    private T data;

    private static final int SUCCESS = 200;

    public static<T> ApiResponse<T> success(String message){
        return new ApiResponse<T>(SUCCESS, "true", message, null);
    }

    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<T>(SUCCESS, "true", message, data);
    }

    public static <T> ApiResponse<T> fail(ResponseCode responseCode, T data){
        return new ApiResponse<T>(responseCode.getHttpStatusCode(), "false", responseCode.getMessage(), data);
    }
}
