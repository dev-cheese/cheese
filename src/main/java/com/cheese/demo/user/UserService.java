package com.cheese.demo.user;

import com.cheese.demo.commons.CommonDto;
import com.cheese.demo.user.exception.EmailDuplicationException;
import com.cheese.demo.user.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // TODO: 2018. 1. 31. 비밀번호 및 이메일 발리데이션 추가할것
    // TODO: 2018. 1. 31. 모델 라이브러리 사용하면 더 좋을 거같음
    public User create(UserDto.SignUpReq dto) {
        final String email = dto.getEmail();

        if (isDuplicatedEmail(email))
            throw new EmailDuplicationException(email);


        dto.setPassword(encodePassword(dto.getPassword()));

        return userRepository.save(modelMapper.map(dto, User.class));
    }

    // TODO: 2018. 2. 6. 접근 권한 추가해야함 -yun

    public User update(Long id, UserDto.MyAccountReq dto) {
        User user = findById(id);
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setMobile(dto.getMobile());
        user.setDob(dto.getDob());
        return user;
    }

    public PageImpl<UserDto.Res> findAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        List<UserDto.Res> content = convertResDto(page);
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    public User findById(Long id) {
        User user = userRepository.findOne(id);
        if (user != null)
            return user;
        else
            throw new UserNotFoundException(id);
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null)
            return user;
        else
            throw new UserNotFoundException(email);
    }

    public CommonDto.ExistenceRes isExist(String email) {
        CommonDto.ExistenceRes existence = new CommonDto.ExistenceRes();
        existence.setExistence(isDuplicatedEmail(email));
        return existence;
    }

    private boolean isDuplicatedEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private List<UserDto.Res> convertResDto(Page<User> page) {
        return page.getContent()
                .parallelStream()
                .map(user -> modelMapper.map(user, UserDto.Res.class))
                .collect(Collectors.toList());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
