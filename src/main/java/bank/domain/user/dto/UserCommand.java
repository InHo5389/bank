package bank.domain.user.dto;

import bank.domain.user.User;
import bank.domain.user.UserEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserCommand {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Join {
        private String username;
        private String password;
        private String email;
        private String fullname;

        public User toEntity(PasswordEncoder passwordEncoder){
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullName(fullname)
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    }
}
