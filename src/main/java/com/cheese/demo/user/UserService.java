package com.cheese.demo.user;

import com.cheese.demo.user.exception.EmailDuplicationException;
import com.cheese.demo.user.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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


    // TODO: 2018. 1. 31. 비밀번호 및 이메일 발리데이션 추가할것
    // TODO: 2018. 1. 31. 모델 라이브러리 사용하면 더 좋을 거같음
    public User create(UserDto.SignUp dto) {
        final String email = dto.getEmail();

        if (isDuplicatedEmail(email))
            throw new EmailDuplicationException(email);

        return userRepository.save(modelMapper.map(dto, User.class));
    }

    // TODO: 2018. 2. 6. 접근 권한 추가해야함 -yun
    public User update(Long id, UserDto.MyAccount dto) {
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

    public UserDto.Existence isExist(String email) {
        UserDto.Existence existence = new UserDto.Existence();
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
}
