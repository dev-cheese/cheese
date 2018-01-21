package com.cheese.demo.user.service;

import com.cheese.demo.user.domain.User;
import com.cheese.demo.user.domain.UserRepository;
import com.cheese.demo.user.dto.UserSignUpReqDto;
import com.cheese.demo.user.dto.UserUpdateReqDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Transactional
    public User signUp(UserSignUpReqDto dto) {
        return userRepository.save(dto.toEntity());
    }

    @Transactional
    public User updated(UserUpdateReqDto dto) {
        return userRepository.save(dto.toEntity());
    }

}
