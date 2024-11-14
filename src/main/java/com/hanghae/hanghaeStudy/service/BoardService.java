package com.hanghae.hanghaeStudy.service;

import com.hanghae.hanghaeStudy.dto.board.BoardRequestDto;
import com.hanghae.hanghaeStudy.dto.board.BoardResponseDto;
import com.hanghae.hanghaeStudy.entity.Board;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.repository.BoardRepository;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<BoardResponseDto> findAll(){
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(BoardResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardResponseDto findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        return BoardResponseDto.toDto(board);
    }

    @Transactional
    public BoardResponseDto save(BoardRequestDto boardRequestDto, String userName) {
        User user = userRepository.findByUsername(userName);
        boardRequestDto.setUser(user);
        Board board = boardRepository.save(Board.toEntity(boardRequestDto));
        if(board == null){
            throw new RuntimeException();
        }
        return BoardResponseDto.toDto(board);
    }

    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(id).orElseThrow(RuntimeException::new);
        board.updateBoard(boardRequestDto);
        return BoardResponseDto.toDto(board);
    }

    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(RuntimeException::new);
        boardRepository.delete(board);
    }
}
