package com.cheese.demo.user;


import com.cheese.demo.commons.vo.PageVo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Page<UserDto.Res> getUsers(PageVo pageVo) {
        return userService.findAll(pageVo.makePageable(0, "id"));
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

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public UserDto.Existence isExist(@RequestParam(value = "email") String email) {
        return userService.isExist(email);
    }
}
