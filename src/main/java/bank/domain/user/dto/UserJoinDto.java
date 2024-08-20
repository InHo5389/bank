package bank.domain.user.dto;

import bank.domain.user.User;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinDto {


    private Long id;
    private String username;
    private String fullname;

    public static UserJoinDto fromEntity(User user) {
        return UserJoinDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullname(user.getFullName())
                .build();
    }
}
