package com.hanghae.hanghaeStudy.controller;

import com.hanghae.hanghaeStudy.dto.board.BoardDeleteDto;
import com.hanghae.hanghaeStudy.dto.board.BoardRequestDto;
import com.hanghae.hanghaeStudy.dto.board.BoardResponseDto;
import com.hanghae.hanghaeStudy.dto.board.BoardUpdateDto;
import com.hanghae.hanghaeStudy.entity.Board;
import com.hanghae.hanghaeStudy.response.ApiResponse;
import com.hanghae.hanghaeStudy.response.ResponseCode;
import com.hanghae.hanghaeStudy.security.TokenProvider;
import com.hanghae.hanghaeStudy.service.BoardService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final TokenProvider tokenProvider;

    @GetMapping("/boards")
    public ApiResponse<List<BoardResponseDto>> getBoards(){
        List<BoardResponseDto> boardResponseDtos = boardService.findAll();
        return ApiResponse.success(ResponseCode.BOARD_ALL_READ_SUCCESS.getMessage(), boardResponseDtos);
    }

    @GetMapping("/boardDetail/{id}")
    public ApiResponse<BoardResponseDto> getBoardById(@PathVariable("id") Long id){
        BoardResponseDto boardResponseDto = boardService.findById(id);
        return ApiResponse.success(ResponseCode.BOARD_READ_SUCCESS.getMessage(), boardResponseDto);
    }

    @PostMapping("/board")
    public ApiResponse<BoardResponseDto> boardPost(@RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request){
        // header accessToken에서 읽어올 예정
        String authorizationHeader = request.getHeader("Authorization");
        String username = "";
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);
            Claims claims = tokenProvider.parseClaims(token);
            username = claims.getSubject();
        }

        BoardResponseDto boardResponseDto = boardService.save(boardRequestDto, username);
        return ApiResponse.success(ResponseCode.BOARD_CREATE_SUCCESS.getMessage(), boardResponseDto);
    }

    @PutMapping("/board/{id}")
    public ApiResponse<BoardResponseDto> boardPut(@PathVariable("id") Long id, @RequestBody BoardUpdateDto boardUpdateDto, HttpServletRequest request){
        BoardResponseDto boardResponseDto = boardService.update(id, boardUpdateDto);
        return ApiResponse.success(ResponseCode.BOARD_UPDATE_SUCCESS.getMessage(), boardResponseDto);
    }

    @DeleteMapping("/board/{id}")
    public ApiResponse<BoardResponseDto> boardDelete(@PathVariable("id") Long id, @RequestBody BoardDeleteDto boardDeleteDto){
        boardService.delete(id, boardDeleteDto);
        return ApiResponse.success(ResponseCode.BOARD_DELETE_SUCCESS.getMessage(), null);
    }
}
