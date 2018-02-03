package com.cheese.demo.user;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto.SignUp signUp(@RequestBody @Valid UserDto.SignUp dto) {
        return modelMapper.map(userService.create(dto), UserDto.SignUp.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public UserDto.MyAccount update(@PathVariable Long id, @RequestBody UserDto.MyAccount dto) {
        return modelMapper.map(userService.update(id, dto), UserDto.MyAccount.class);
    }

}
