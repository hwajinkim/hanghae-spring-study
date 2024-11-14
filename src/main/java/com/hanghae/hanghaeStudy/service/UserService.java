package com.hanghae.hanghaeStudy.service;

import com.hanghae.hanghaeStudy.dto.user.UserRequestDto;
import com.hanghae.hanghaeStudy.dto.user.UserResponseDto;
import com.hanghae.hanghaeStudy.entity.User;
import com.hanghae.hanghaeStudy.exception.AlreadyExistUserException;
import com.hanghae.hanghaeStudy.exception.IncorrectPasswordException;
import com.hanghae.hanghaeStudy.exception.UsernameNotFoundException;
import com.hanghae.hanghaeStudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto signup(UserRequestDto userRequestDto){
        if(userRepository.existsByUsername(userRequestDto.getUsername())){
            throw new AlreadyExistUserException("이미 존재하는 회원입니다. :"+ userRequestDto.getUsername());
        }
        return UserResponseDto.toDto(userRepository.save(User.toEntity(userRequestDto)));
    }

    @Transactional(readOnly = true)
    public UserResponseDto signIn(UserRequestDto userRequestDto){
        User user = userRepository.findByUsername(userRequestDto.getUsername());

        if(user == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다. :  "+userRequestDto.getUsername());
        }

        if(!user.getPassword().equals(userRequestDto.getPassword())){
            throw new IncorrectPasswordException("비밀번호가 일치하지 않습니다.:  "+userRequestDto.getUsername());
        }

        return UserResponseDto.toDto(user);
    }
}
