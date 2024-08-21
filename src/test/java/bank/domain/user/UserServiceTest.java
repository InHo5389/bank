package bank.domain.user;

import bank.common.config.dummy.DummyObject;
import bank.domain.common.exception.CustomGlobalException;
import bank.domain.user.dto.UserCommand;
import bank.domain.user.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

// 스프링 관련 빈들이 하나도 없는 환경
@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    /**
     * 가짜가 아닌 진짜로 띄우기 위해 spy사용
     * 스프링 ioc에 있는 진짜 빈을 가져와 UserService에 주입
     */
    @Spy
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입시 중복된 아이디가 없으면 회원가입이 정상적으로 된다.")
    void join_success() {
        //given
        UserCommand.Join command =
                new UserCommand.Join("ssar", "1234", "12@naver.com", "쌀");
        Mockito.when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());
        User ssar = newMockUser(1L,"ssar","쌀");
        Mockito.when(userRepository.save(any()))
                .thenReturn(ssar);
        //when
        UserResponse.Join joinResponse = userService.join(command);
        //then
        assertThat(joinResponse).extracting("id","username","fullname")
                .containsExactlyInAnyOrder(1L,"ssar","쌀");
    }

    @Test
    @DisplayName("회원가입시 중복된 아이디가 있으면 예외가 발생한다.")
    void join_fail() {
        //given
        UserCommand.Join command =
                new UserCommand.Join("ssar", "1234", "12@naver.com", "쌀");
        Mockito.when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(new User()));
        //when
        //then
        assertThatThrownBy(()->userService.join(command))
                .isInstanceOf(CustomGlobalException.class)
                .hasMessage("중복된 아이디입니다. 다른 아이디를 사용해 주세요.");
    }
}