package bank.controller.user.dto;

import bank.domain.user.dto.UserCommand;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserRequest {

    @Getter
    @AllArgsConstructor
    public static class Join {

        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$",message = "영문/숫자만 이용 가능하고 2~20자 이내로 작성해주세요.")
        @NotEmpty(message = "아이디를 입력해주세요.") // null 이거나, 공백일 수 없다.
        private String username;
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        @Size(min = 4,max = 20)
        private String password;
        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$",message = "20자이하, 이메일 형식으로 작성하여 주세요.")
        @NotEmpty(message = "이메일을 입력해주세요.")
        private String email;
        @NotEmpty(message = "이름을 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$",message = "한글/영문 1~20자 이내로 작성해주세요.")
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
