package com.cheese.demo.user.controller;


import com.cheese.demo.user.domain.User;
import com.cheese.demo.user.domain.UserRepository;
import com.cheese.demo.user.dto.UserSignUpReqDto;
import com.cheese.demo.user.dto.UserUpdateReqDto;
import com.cheese.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public User signUp(@RequestBody UserSignUpReqDto dto) {
        return userService.signUp(dto);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Page<User> signUp(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public User update(@RequestBody UserUpdateReqDto dto) {
        return userService.updated(dto);
    }
}
