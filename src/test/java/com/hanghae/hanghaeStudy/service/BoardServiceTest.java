package com.hanghae.hanghaeStudy.service;

import com.hanghae.hanghaeStudy.entity.Board;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.exception.BoardSaveFailureException;
import com.hanghae.hanghaeStudy.exception.IncorrectPasswordException;
import com.hanghae.hanghaeStudy.exception.UsernameNotFoundException;
import com.hanghae.hanghaeStudy.repository.BoardRepository;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hanghae.hanghaeStudy.dto.board.BoardResponseDto;
import com.hanghae.hanghaeStudy.dto.board.BoardRequestDto;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    @DisplayName("모든 게시글 조회 성공 테스트")
    void findAllSuccess(){
        // Given
        User mockUser = new User( 1L,"testUser", "password123!");

        List<Board> mockBoards = List.of(
                new Board(1L, mockUser,"title 1", "content1"),
                new Board(1L, mockUser,"title 2", "content2")
        );
        when(boardRepository.findAll()).thenReturn(mockBoards);

        // When
        List<BoardResponseDto> responseDtos = boardService.findAll();

        // Then
        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());
        assertEquals("title 1", responseDtos.get(0).getTitle());
        assertEquals("title 2", responseDtos.get(1).getTitle());
        verify(boardRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("특정 게시글 조회 성공 테스트")
    void findByIdSuccess(){
        // Given
        User mockUser = new User( 1L,"testUser", "password123!");
        Long boardId = 1L;
        Board mockBoard = new Board(boardId, mockUser, "title 1", "content 1");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));

        // When
        BoardResponseDto responseDto = boardService.findById(boardId);

        // Then
        assertNotNull(responseDto);
        assertEquals("title 1", responseDto.getTitle());
        assertEquals("content 1", responseDto.getContent());
        verify(boardRepository, times(1)).findById(boardId);
    }

    @Test
    @DisplayName("특정 게시글 조회 실패 테스트 - 존재하지 않는 게시글")
    void findByIdFail(){
        // Given
        Long boardId = 1L;

        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());
        // When
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> boardService.findById(boardId));

        assertNotNull(exception);
        verify(boardRepository, times(1)).findById(boardId);
    }

    @Test
    @DisplayName("게시글 저장 성공 테스트")
    void saveSuccess(){
        // Given
        String userName = "testUser";
        User mockUser = new User(1L, "testUser", "password123!");
        BoardRequestDto requestDto = new BoardRequestDto( "title", "content", mockUser);
        Board mockBoard = new Board(1L, mockUser, "title", "content");

        when(userRepository.findByUsername(userName)).thenReturn(mockUser);
        when(boardRepository.save(any(Board.class))).thenReturn(mockBoard);

        // When
        BoardResponseDto responseDto = boardService.save(requestDto, userName);

        // Then
        assertNotNull(responseDto);
        assertEquals("title", responseDto.getTitle());
        verify(userRepository, times(1)).findByUsername(userName);
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("게시글 저장 실패 테스트 - 사용자 없음")
    void saveFailure_UserNotFound(){
        // Given
        String userName = "unknownUser";
        BoardRequestDto requestDto = new BoardRequestDto( "title", "content", null);

        when(userRepository.findByUsername(userName)).thenReturn(null);

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
            ()-> boardService.save(requestDto, userName));

        assertEquals("사용자를 찾을 수 없습니다. : unknownUser", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(userName);
        verify(boardRepository, never()).save(any(Board.class));
    }

    @Test
    @DisplayName("게시글 저장 실패 테스트 - 게시글 저장 실패")
    void saveFailure_BoardSaveFailure(){
        // Given
        String userName = "testUser";
        User mockUser = new User(1L, "testUser", "password123!");
        BoardRequestDto requestDto = new BoardRequestDto( "title", "content", mockUser);

        when(userRepository.findByUsername(userName)).thenReturn(mockUser);
        when(boardRepository.save(any(Board.class))).thenReturn(null);

        // When & Then
        BoardSaveFailureException exception = assertThrows(BoardSaveFailureException.class,
                ()-> boardService.save(requestDto, userName));

        assertEquals("게시글 등록에 실패하였습니다.", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(userName);
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("게시글 수정 성공 테스트")
    void updateSuccess(){
        // Given
        Long boardId = 1L;
        String password = "password123!";
        User mockUser = new User(1L, "testUser", password);
        Board mockBoard = new Board(boardId, mockUser, "Old title", "Old content");
        BoardRequestDto updateDto = new BoardRequestDto("Updated title", "Updated content", mockUser);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));

        // When
        BoardResponseDto responseDto = boardService.update(boardId, updateDto, password);

        assertNotNull(responseDto);
        assertEquals("Updated title", responseDto.getTitle());
        verify(boardRepository, times(1)).findById(boardId);
    }

    @Test
    @DisplayName("게시글 수정 실패 테스트 - 비밀번호 불일치")
    void updateFailureDueToPasswordMismatch(){
        // Given
        Long boardId = 1L;
        String correctPassword = "password123!";
        String wrongPassword = "wrongPassword";
        User mockUser = new User(1L, "testUser", correctPassword);
        Board mockBoard = new Board(boardId, mockUser, "Old title", "Old content");
        BoardRequestDto updateDto = new BoardRequestDto("Updated title", "Updated content", mockUser);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));

        // When & Then
        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class,
                () -> boardService.update(boardId, updateDto, wrongPassword));

        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(boardRepository, times(1)).findById(boardId);
    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void deleteSuccess(){
        // Given
        Long boardId = 1L;
        String password = "password123!";
        User mockUser = new User(1L, "testUser", password);
        Board mockBoard = new Board(boardId, mockUser, "title", "content");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));

        // When
        boardService.delete(boardId, password);

        // Then
        verify(boardRepository, times(1)).findById(boardId);
        verify(boardRepository, times(1)).delete(mockBoard);
    }

    @Test
    @DisplayName("게시글 삭제 실패 테스트 - 비밀번호 불일치")
    void deleteFailureDueToPasswordMismatch(){
        // Given
        Long boardId = 1L;
        String correctPassword = "password123!";
        String wrongPassword = "wrongPassword";
        User mockUser = new User(1L, "testUser", correctPassword);
        Board mockBoard = new Board(boardId, mockUser, "title", "content");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));

        // When & Then
        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class,
                () -> boardService.delete(boardId, wrongPassword));

        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(boardRepository, times(1)).findById(boardId);
        verify(boardRepository, never()).delete(any(Board.class));
    }
}
