package bank.domain.account.dto;

import bank.domain.account.Account;
import bank.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

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

    @Getter
    public static class GetByUser {
        private Long userId;
        private String fullname;
        private List<AccountDto> accounts;

        public GetByUser(User user, List<Account> accounts) {
            this.userId = user.getId();
            this.fullname = user.getFullName();
            this.accounts = accounts.stream().map(AccountDto::new)
                    .collect(Collectors.toList());
        }

        @Getter
        public class AccountDto{
            private Long accountId;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.accountId = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }
}
