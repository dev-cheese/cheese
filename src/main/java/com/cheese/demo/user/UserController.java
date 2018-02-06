package com.cheese.demo.user;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public UserDto.Res signUp(@RequestBody @Valid UserDto.SignUp dto) {
        return modelMapper.map(userService.create(dto), UserDto.Res.class);
    }

    // TODO: 2018. 2. 6. pageable vo 객체 만들것 -yun
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Page<UserDto.Res> getUsers(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public UserDto.Res getUser(@PathVariable Long id) {
        return modelMapper.map(userService.findById(id), UserDto.Res.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public UserDto.MyAccount update(@PathVariable Long id, @RequestBody UserDto.MyAccount dto) {
        return modelMapper.map(userService.update(id, dto), UserDto.MyAccount.class);
    }
}
