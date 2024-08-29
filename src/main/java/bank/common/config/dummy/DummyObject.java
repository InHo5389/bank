package bank.common.config.dummy;

import bank.domain.account.Account;
import bank.domain.transaction.Transaction;
import bank.domain.transaction.TransactionEnum;
import bank.domain.user.User;
import bank.domain.user.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {

    protected User newUser(String username, String fullname) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode("1234");
        return User.builder()
                .username(username)
                .password(encodePassword)
                .email(username + "@naver.com")
                .fullName(fullname)
                .role(UserEnum.CUSTOMER)
                .build();
    }

    protected User newMockUser(long id, String username, String fullname) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode("1234");
        return User.builder()
                .id(id)
                .username(username)
                .password(encodePassword)
                .email(username + "@naver.com")
                .fullName(fullname)
                .role(UserEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Account newAccount(Long number, User user) {
        return Account.builder()
                .number(number)
                .password(1234L)
                .balance(1000L)
                .user(user)
                .build();
    }

    protected Account newMockAccount(Long id, Long number, User user, Long balance) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(1234L)
                .balance(balance)
                .user(user)
                .build();
    }

    protected Transaction newDepositTransaction(Account account, Long amount, String receiver, String tel) {
        return Transaction.builder()
                .depositAccount(account)
                .withdrawAccount(null)
                .depositAccountBalance(account.getBalance())
                .withdrawAccountBalance(null)
                .amount(amount)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(receiver)
                .tel(tel)
                .build();
    }

    protected Transaction newMockDepositTransaction(Long id, Account account, Long amount, String receiver, String tel) {
        account.deposit(amount);
        return Transaction.builder()
                .id(id)
                .depositAccount(account)
                .withdrawAccount(null)
                .depositAccountBalance(account.getBalance())
                .withdrawAccountBalance(null)
                .amount(amount)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(receiver)
                .tel(tel)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newMockWithdrawTransaction(Long id, Account account, Long amount) {
        account.withdraw(amount);
        return Transaction.builder()
                .id(id)
                .withdrawAccount(account)
                .depositAccount(null)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .amount(amount)
                .gubun(TransactionEnum.WITHDRAW)
                .sender(account.getNumber() + "")
                .receiver("ATM")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
