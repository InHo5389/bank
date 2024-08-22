package bank.domain.user.dto;

import bank.common.util.DateUtil;
import bank.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;

public class UserResponse {

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Join{
        private Long id;
        private String username;
        private String fullname;

        public static UserResponse.Join toResponse(User user) {
            return UserResponse.Join.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .fullname(user.getFullName())
                    .build();
        }
    }

    @Getter
    public static class Login{
        private Long id;
        private String username;
        private String token;
        private String createdAt;

        public Login(User user,String token) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.token = token;
            this.createdAt = DateUtil.toStringFormat(user.getCreatedAt());
        }
    }
}
