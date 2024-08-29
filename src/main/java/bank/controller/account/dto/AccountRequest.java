package bank.controller.account.dto;

import bank.domain.account.dto.AccountCommand;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountRequest {

    @Getter
    @AllArgsConstructor
    public static class Create {

        @NotNull
        // 사이즈로 하면 안됨, 사이즈는 string한테 하는 것
        // 최대 4자리 까지(임시)
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        public AccountCommand.Create toCommand() {
            return AccountCommand.Create.builder()
                    .number(number)
                    .password(password)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Deposit {

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;
        @NotNull
        @Positive(message = "1원 이상 입금하실수 있습니다.")
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "^(DEPOSIT)$")
        private String gubun;
        @NotEmpty
        @Pattern(regexp = "^[0-9]{3}-[0-9]{4}-[0-9]{4}")
        private String tel;

        public AccountCommand.Deposit toCommand(){
            return AccountCommand.Deposit.builder()
                    .number(number)
                    .amount(amount)
                    .gubun(gubun)
                    .tel(tel)
                    .build();
        }
    }


    @Getter
    @AllArgsConstructor
    public static class Withdraw{
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;
        @NotNull
        @Positive(message = "1원 이상 입금하실수 있습니다.")
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "^(WITHDRAW)$")
        private String gubun;

        public AccountCommand.Withdraw toCommand(){
            return AccountCommand.Withdraw.builder()
                    .number(number)
                    .password(password)
                    .amount(amount)
                    .gubun(gubun)
                    .build();
        }
    }
}
