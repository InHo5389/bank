package bank.controller.user.dto;

import bank.domain.user.dto.UserCommand;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserRequest {

    @Getter
    @AllArgsConstructor
    public static class Join {

        @Pattern(regexp = "",message = "영문/숫자만 이용 가능하고 2~20자 이내로 작성해주세요.")
        @NotEmpty(message = "아이디를 입력해주세요.") // null 이거나, 공백일 수 없다.
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String email;
        @NotEmpty
        private String fullname;

        public UserCommand.Join toCommand() {
            return UserCommand.Join.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .fullname(fullname)
                    .build();
        }
    }
}
