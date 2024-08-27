package bank.domain.account.dto;

import bank.domain.account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AccountResponse {

    @Getter
    public static class Create{
        Long accountId;
        private Long number;
        private Long balance;

        public Create(Account account) {
            this.accountId = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }
}
