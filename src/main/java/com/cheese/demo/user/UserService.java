package com.cheese.demo.user;

import com.cheese.demo.user.exception.EmailDuplicatedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

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
            throw new EmailDuplicatedException(email);

        return userRepository.save(modelMapper.map(dto, User.class));
    }

    public User update(Long id, UserDto.MyAccount dto) {
        return userRepository.save(User.builder()
                .id(findById(id).getId())
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .mobile(dto.getMobile())
                .dob(dto.getDob())
                .build());
    }

    public User findById(Long id) {
        User user = userRepository.findOne(id);
        if (user != null)
            return user;
        else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    private boolean isDuplicatedEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
