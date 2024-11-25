package com.hanghae.hanghaeStudy.service;

import com.hanghae.hanghaeStudy.dto.board.BoardRequestDto;
import com.hanghae.hanghaeStudy.dto.board.BoardResponseDto;
import com.hanghae.hanghaeStudy.entity.Board;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.exception.BoardSaveFailureException;
import com.hanghae.hanghaeStudy.exception.IncorrectPasswordException;
import com.hanghae.hanghaeStudy.exception.UsernameNotFoundException;
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
        User user = userRepository.findByUsername(userName)
                .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다. : "+userName));
        boardRequestDto.setUser(user);
        Board board = boardRepository.save(Board.toEntity(boardRequestDto));
        if(board == null){
            throw new BoardSaveFailureException("게시글 등록에 실패하였습니다.");
        }
        return BoardResponseDto.toDto(board);
    }

    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto boardRequestDto, String password) {
        Board board = boardRepository.findById(id).orElseThrow(RuntimeException::new);
        if(!board.getUser().getPassword().equals(password)){
            throw new IncorrectPasswordException("비밀번호가 일치하지 않습니다.");
        }

        board.updateBoard(boardRequestDto);
        return BoardResponseDto.toDto(board);
    }

    @Transactional
    public void delete(Long id, String password) {
        Board board = boardRepository.findById(id).orElseThrow(RuntimeException::new);
        if(!board.getUser().getPassword().equals(password)){
            throw new IncorrectPasswordException("비밀번호가 일치하지 않습니다.");
        }

        boardRepository.delete(board);
    }
}
