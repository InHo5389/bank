package bank.domain.account.dto;

import bank.common.util.DateUtil;
import bank.domain.account.Account;
import bank.domain.transaction.Transaction;
import bank.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Getter
    public static class Deposit{
        private Long accountId;
        private Long number;
        private TransactionDto transaction;

        public Deposit(Account account, Transaction transaction) {
            this.accountId = account.getId();
            this.number = account.getNumber();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        public class TransactionDto{
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;

            @JsonIgnore
            private Long depositAccountBalance;
            private String tel;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = DateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter
    public static class Withdraw{
        private Long accountId;
        private Long number;
        private Long balance;
        private TransactionDto transaction;

        public Withdraw(Account account, Transaction transaction) {
            this.accountId = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new Withdraw.TransactionDto(transaction);
        }

        @Getter
        public class TransactionDto{
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = DateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter
    public static class Transfer{
        private Long accountId;
        private Long number;
        private Long balance;
        private TransactionDto transaction;

        public Transfer(Account account, Transaction transaction) {
            this.accountId = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new Transfer.TransactionDto(transaction);
        }

        @Getter
        public class TransactionDto{
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = DateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }
}
