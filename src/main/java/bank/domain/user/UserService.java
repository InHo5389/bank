package bank.domain.user;

import bank.domain.common.exception.CustomGlobalException;
import bank.domain.common.exception.ErrorType;
import bank.domain.user.dto.UserCommand;
import bank.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto.Join join(UserCommand.Join command) {
        // 1. 동일 유저네임 존재 검사
        Optional<User> userOptional = userRepository.findByUsername(command.getUsername());
        if (userOptional.isPresent()){
            throw new CustomGlobalException(ErrorType.DUPLICATE_USERNAME);
        }

        // 2. 패스워드 인코딩 + 회원가입
        User savedUser = userRepository.save(command.toEntity(passwordEncoder));

        // 3. dto 응답
        return UserDto.Join.fromEntity(savedUser);
    }
}
