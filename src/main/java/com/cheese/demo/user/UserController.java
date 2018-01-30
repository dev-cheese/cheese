package com.cheese.demo.user;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto.SignUpRes signUp(@RequestBody UserDto.SignUpReq dto) {
        return modelMapper.map(userService.create(dto), UserDto.SignUpRes.class);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseStatus(value = HttpStatus.OK)
    public UserDto.UpdateRes update(@PathVariable Long id, @RequestBody UserDto.UpdateReq dto) {
        return modelMapper.map(userService.update(id, dto), UserDto.UpdateRes.class);
    }

}
