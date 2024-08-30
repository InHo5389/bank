package bank.domain.transaction.dto;

import bank.common.util.DateUtil;
import bank.domain.account.Account;
import bank.domain.transaction.Transaction;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionResponse {

    @Getter
    public static class History {
        private List<TransactionDto> transactions;

        public History(Account account, List<Transaction> transactions) {
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
