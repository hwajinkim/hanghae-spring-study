package com.hanghae.hanghaeStudy.unitTest.service;

import com.hanghae.hanghaeStudy.dto.board.BoardDeleteDto;
import com.hanghae.hanghaeStudy.dto.board.BoardUpdateDto;
import com.hanghae.hanghaeStudy.entity.Board;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.exception.BoardSaveFailureException;
import com.hanghae.hanghaeStudy.exception.IncorrectPasswordException;
import com.hanghae.hanghaeStudy.exception.UsernameNotFoundException;
import com.hanghae.hanghaeStudy.repository.BoardRepository;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import com.hanghae.hanghaeStudy.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hanghae.hanghaeStudy.dto.board.BoardResponseDto;
import com.hanghae.hanghaeStudy.dto.board.BoardRequestDto;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
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

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("모든 게시글 조회 성공 테스트")
    void findAllSuccess(){
        // Given
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        User mockUser = new User( 1L,"testUser", "password123!", roles);

        List<Board> mockBoards = List.of(
                new Board(1L, mockUser,"title 1", "content1"),
                new Board(1L, mockUser,"title 2", "content2")
        );
        when(boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))).thenReturn(mockBoards);

        // When
        List<BoardResponseDto> responseDtos = boardService.findAll();

        // Then
        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());
        assertEquals("title 1", responseDtos.get(0).getTitle());
        assertEquals("title 2", responseDtos.get(1).getTitle());
        verify(boardRepository, times(1)).findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Test
    @DisplayName("특정 게시글 조회 성공 테스트")
    void findByIdSuccess(){
        // Given
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        User mockUser = new User( 1L,"testUser", "password123!", roles);
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
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        User mockUser = new User(1L, "testUser", "password123!", roles);
        BoardRequestDto requestDto = new BoardRequestDto( "title", "content", mockUser);
        Board mockBoard = new Board(1L, mockUser, "title", "content");

        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(mockUser));
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

        when(userRepository.findByUsername(userName)).thenReturn(Optional.empty());

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
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        User mockUser = new User(1L, "testUser", "password123!", roles);
        BoardRequestDto requestDto = new BoardRequestDto( "title", "content", mockUser);

        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(mockUser));
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
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        User mockUser = new User(1L, "testUser", password, roles);
        Board mockBoard = new Board(boardId, mockUser, "Old title", "Old content");

        BoardUpdateDto updateDto = new BoardUpdateDto("Updated title", "Updated content", password);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));
        when(passwordEncoder.matches(updateDto.getPassword(),mockBoard.getUser().getPassword())).thenReturn(true);

        // When
        BoardResponseDto responseDto = boardService.update(boardId, updateDto);

        assertNotNull(responseDto);
        assertEquals("Updated title", responseDto.getTitle());
        assertEquals("Updated content", responseDto.getContent());
        verify(boardRepository, times(1)).findById(boardId);
    }

    @Test
    @DisplayName("게시글 수정 실패 테스트 - 비밀번호 불일치")
    void updateFailureDueToPasswordMismatch(){
        // Given
        Long boardId = 1L;
        String correctPassword = "password123!";
        String wrongPassword = "wrongPassword";

        User mockUser = new User(1L, "testUser", correctPassword, List.of("USER"));
        Board mockBoard = new Board(boardId, mockUser, "Old title", "Old content");

        BoardUpdateDto updateDto = new BoardUpdateDto("Updated title", "Updated content", wrongPassword);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));
        when(passwordEncoder.matches(updateDto.getPassword(), mockBoard.getUser().getPassword())).thenReturn(false);

        // When & Then
        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class,
                () -> boardService.update(boardId, updateDto));

        assertEquals("비밀번호가 일치하지 않습니다.:  "+updateDto.getPassword(), exception.getMessage());
        verify(boardRepository, times(1)).findById(boardId);
        verify(passwordEncoder, times(1)).matches(updateDto.getPassword(), mockBoard.getUser().getPassword());
    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void deleteSuccess(){
        // Given
        Long boardId = 1L;
        String password = "password123!";

        List<String> roles = new ArrayList<>();
        roles.add("USER");

        User mockUser = new User(1L, "testUser", password, roles);
        Board mockBoard = new Board(boardId, mockUser, "title", "content");
        BoardDeleteDto deleteDto = new BoardDeleteDto(password);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));
        when(passwordEncoder.matches(deleteDto.getPassword(),mockBoard.getUser().getPassword())).thenReturn(true);

        // When
        boardService.delete(boardId, deleteDto);

        // Then
        verify(boardRepository, times(1)).findById(boardId);
        verify(passwordEncoder, times(1)).matches(deleteDto.getPassword(), mockBoard.getUser().getPassword());
        verify(boardRepository, times(1)).delete(mockBoard);
    }

    @Test
    @DisplayName("게시글 삭제 실패 테스트 - 비밀번호 불일치")
    void deleteFailureDueToPasswordMismatch(){
        // Given
        Long boardId = 1L;
        String correctPassword = "password123!";
        String wrongPassword = "wrongPassword";

        User mockUser = new User(1L, "testUser", correctPassword, List.of("USER"));
        Board mockBoard = new Board(boardId, mockUser, "title", "content");

        BoardDeleteDto deleteDto = new BoardDeleteDto(wrongPassword);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));
        when(passwordEncoder.matches(deleteDto.getPassword(), mockBoard.getUser().getPassword())).thenReturn(false);

        // When & Then
        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class,
                () -> boardService.delete(boardId, deleteDto));

        assertEquals("비밀번호가 일치하지 않습니다.:  "+deleteDto.getPassword(), exception.getMessage());
        verify(boardRepository, times(1)).findById(boardId);
        verify(passwordEncoder, times(1)).matches(deleteDto.getPassword(), mockBoard.getUser().getPassword());
        verify(boardRepository, never()).delete(any(Board.class));
    }
}
