package bank.domain.user.dto;

import bank.domain.user.User;
import lombok.*;

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
}
