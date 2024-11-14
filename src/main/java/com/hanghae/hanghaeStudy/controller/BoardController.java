package com.hanghae.hanghaeStudy.controller;

import com.hanghae.hanghaeStudy.dto.board.BoardRequestDto;
import com.hanghae.hanghaeStudy.dto.board.BoardResponseDto;
import com.hanghae.hanghaeStudy.entity.Board;
import com.hanghae.hanghaeStudy.response.ApiResponse;
import com.hanghae.hanghaeStudy.response.ResponseCode;
import com.hanghae.hanghaeStudy.service.BoardService;
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

    @GetMapping("/boards")
    public ApiResponse<List<BoardResponseDto>> getBoards(){
        List<BoardResponseDto> boardResponseDtos = boardService.findAll();
        return ApiResponse.success(ResponseCode.BOARD_ALL_READ_SUCCESS.getMessage(), boardResponseDtos);
    }

    @GetMapping("/board/{id}")
    public ApiResponse<BoardResponseDto> getBoardById(@PathVariable("id") Long id){
        BoardResponseDto boardResponseDto = boardService.findById(id);
        return ApiResponse.success(ResponseCode.BOARD_READ_SUCCESS.getMessage(), boardResponseDto);
    }

    @PostMapping("/board")
    public ApiResponse<BoardResponseDto> boardPost(@RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request){
        // header accessToken에서 읽어올 예정
        String userName = request.getHeader("username");

        BoardResponseDto boardResponseDto = boardService.save(boardRequestDto,userName);
        return ApiResponse.success(ResponseCode.BOARD_CREATE_SUCCESS.getMessage(), boardResponseDto);
    }

    @PutMapping("/board/{id}")
    public ApiResponse<BoardResponseDto> boardPut(@PathVariable("id") Long id, @RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request){
        // header accessToken에서 읽어올 예정
        String password = request.getHeader("password");

        BoardResponseDto boardResponseDto = boardService.update(id, boardRequestDto, password);
        return ApiResponse.success(ResponseCode.BOARD_UPDATE_SUCCESS.getMessage(), boardResponseDto);
    }

    @DeleteMapping("/board/{id}")
    public ApiResponse<BoardResponseDto> boardDelete(@PathVariable("id") Long id, HttpServletRequest request){
        // header accessToken에서 읽어올 예정
        String password = request.getHeader("password");

        boardService.delete(id, password);
        return ApiResponse.success(ResponseCode.BOARD_DELETE_SUCCESS.getMessage(), null);
    }
}
