package bank.domain.user;

import bank.common.exception.CustomGlobalException;
import bank.domain.user.dto.UserCommand;
import bank.domain.user.dto.UserJoinDto;
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
    public UserJoinDto join(UserCommand.Create command) {
        // 1. 동일 유저네임 존재 검사
        Optional<User> userOptional = userRepository.findByUsername(command.getUsername());
        if (userOptional.isPresent()){
            throw new CustomGlobalException("중복된 아이디 입니다. 다른 아이디를 사용하여 주세요.");
        }

        // 2. 패스워드 인코딩 + 회원가입
        User savedUser = userRepository.save(command.toEntity(passwordEncoder));

        // 3. dto 응답
        return UserJoinDto.fromEntity(savedUser);
    }
}
