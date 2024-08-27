package bank.controller.account.dto;

import bank.domain.account.dto.AccountCommand;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AccountRequest {

    @Getter
    @AllArgsConstructor
    public static class Create {

        @NotNull
        // 사이즈로 하면 안됨, 사이즈는 string한테 하는 것
        // 최대 4자리 까지(임시)
        @Digits(integer = 4,fraction = 4)
        private Long number;

        @NotNull
        @Digits(integer = 4,fraction = 4)
        private Long password;

        public AccountCommand.Create toCommand(){
            return AccountCommand.Create.builder()
                    .number(number)
                    .password(password)
                    .build();
        }
    }
}
