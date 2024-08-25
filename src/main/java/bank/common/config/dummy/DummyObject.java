package bank.common.config.dummy;

import bank.domain.user.User;
import bank.domain.user.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {

    public User newUser(String username,String fullname){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode("1234");
        return User.builder()
                .username(username)
                .password(encodePassword)
                .email(username+"@naver.com")
                .fullName(fullname)
                .role(UserEnum.CUSTOMER)
                .build();
    }

    public User newMockUser(long id, String username, String fullname){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode("1234");
        return User.builder()
                .id(id)
                .username(username)
                .password(encodePassword)
                .email(username+"@naver.com")
                .fullName(fullname)
                .role(UserEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
