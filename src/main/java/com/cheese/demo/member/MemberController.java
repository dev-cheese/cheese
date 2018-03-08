package com.cheese.demo.member;


import com.cheese.demo.commons.CommonDto;
import com.cheese.demo.commons.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("members")
public class MemberController {

    @Autowired
    private MemberServiceImpl memberService;


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public MemberDto.Res signUp(@RequestBody @Valid MemberDto.SignUpReq dto) {
        return new MemberDto.Res(memberService.create(dto));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Page<MemberDto.Res> getUsers(PageVo pageVo) {
        return memberService.findAll(pageVo.makePageable(0, "id"));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Res getUser(@PathVariable Long id) {
        return new MemberDto.Res(memberService.findById(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Res update(@PathVariable Long id, @RequestBody MemberDto.MyAccountReq dto) {
        return new MemberDto.Res(memberService.update(id, dto));
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public CommonDto.ExistenceRes isExist(@RequestParam(value = "email") String email) {
        return new CommonDto.ExistenceRes(memberService.isExistedEmail(email));
    }
}
