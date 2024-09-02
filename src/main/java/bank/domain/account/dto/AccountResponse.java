package bank.domain.account.dto;

import bank.common.util.DateUtil;
import bank.domain.account.Account;
import bank.domain.transaction.Transaction;
import bank.domain.transaction.dto.TransactionResponse;
import bank.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Getter
    public static class Detail{

        private Long id;
        private Long number;
        private Long balance;
        private List<TransactionDto> transactions;

        public Detail(Account account, List<Transaction> transactions) {
            this.transactions = transactions.stream()
                    .map((transaction)->new TransactionDto(transaction, account.getNumber()))
                    .collect(Collectors.toList());
        }

        @Getter
        public class TransactionDto{
            private Long id;
            private String gubun;
            private Long amount;
            private String sender;
            private String receiver;
            private String tel;
            private String createdAt;
            private Long balance;

            public TransactionDto(Transaction transaction, Long accountNumber) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.amount = transaction.getAmount();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.tel = transaction.getTel()==null ? "없음" : transaction.getTel();
                this.createdAt = DateUtil.toStringFormat(transaction.getCreatedAt());
                if (transaction.getDepositAccount() == null){
                    this.balance = transaction.getWithdrawAccountBalance();
                }else if (transaction.getWithdrawAccount() == null){
                    this.balance = transaction.getWithdrawAccountBalance();
                }else {
                    // 1111계좌의 입출금 내역 (출금계좌 = 값,입금계좌 = 값)
                    if (accountNumber.longValue() == transaction.getDepositAccount().getNumber()){
                        this.balance = transaction.getDepositAccountBalance();
                    }else {
                        this.balance = transaction.getWithdrawAccountBalance();
                    }
                }
            }
        }
    }
}
