package bank.common.config.auth;

import bank.domain.user.User;
import bank.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    // 없으면 오류 있으면 정상적으로 시큐리티 컨텍스트 내부 세션에 로그인된 세션이 만들어짐
    // 곡 InternalAuthenticationServiceException로 예외를 터트려야함
    // 인증이 되다가 터지면 시큐리티 타고 있을 경우 개발자한테 제어권이 없어서
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalAuthenticationServiceException(username + "인증 실패"));
        // 이게 세션에 만들어짐
        return new LoginUser(user);
    }
}
