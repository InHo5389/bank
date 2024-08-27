package bank.common.config.dummy;

import bank.domain.account.Account;
import bank.domain.user.User;
import bank.domain.user.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {

    protected User newUser(String username, String fullname) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode("1234");
        return User.builder()
                .username(username)
                .password(encodePassword)
                .email(username + "@naver.com")
                .fullName(fullname)
                .role(UserEnum.CUSTOMER)
                .build();
    }

    protected User newMockUser(long id, String username, String fullname) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode("1234");
        return User.builder()
                .id(id)
                .username(username)
                .password(encodePassword)
                .email(username + "@naver.com")
                .fullName(fullname)
                .role(UserEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Account newAccount(Long number, User user) {
        return Account.builder()
                .number(number)
                .password(1234L)
                .balance(1000L)
                .user(user)
                .build();
    }

    protected Account newMockAccount(Long id, Long number, User user, Long balance) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(1234L)
                .balance(balance)
                .user(user)
                .build();
    }
}
