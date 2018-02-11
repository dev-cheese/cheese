package com.cheese.demo.member;


import com.cheese.demo.commons.CommonDto;
import com.cheese.demo.commons.vo.PageVo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ModelMapper modelMapper;


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public MemberDto.Res signUp(@RequestBody @Valid MemberDto.SignUpReq dto) {
        return modelMapper.map(memberService.create(dto), MemberDto.Res.class);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Page<MemberDto.Res> getUsers(PageVo pageVo) {
        return memberService.findAll(pageVo.makePageable(0, "id"));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Res getUser(@PathVariable Long id) {
        return modelMapper.map(memberService.findById(id), MemberDto.Res.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.MyAccountReq update(@PathVariable Long id, @RequestBody MemberDto.MyAccountReq dto) {
        return modelMapper.map(memberService.update(id, dto), MemberDto.MyAccountReq.class);
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public CommonDto.ExistenceRes isExist(@RequestParam(value = "email") String email) {
        return memberService.isExist(email);
    }
}
