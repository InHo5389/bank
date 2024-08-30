package bank.common.config.dummy;

import bank.domain.account.Account;
import bank.domain.account.AccountRepository;
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

    protected Transaction newMockTransferTransaction(Long id, Account withdrawAccount,Account depositAccount, Long amount) {
        withdrawAccount.withdraw(amount);
        depositAccount.deposit(amount);
        return Transaction.builder()
                .id(id)
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(null)
                .amount(amount)
                .gubun(TransactionEnum.TRANSFER)
                .sender(withdrawAccount.getNumber() + "")
                .receiver(depositAccount.getNumber() + "")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository) {
        account.deposit(100L);
        // 더티체킹이 안되기 때문에!!
        if (accountRepository != null) {
            accountRepository.save(account);
        }
        Transaction transaction = Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber() + "")
                .tel("010-2222-7777")
                .build();
        return transaction;
    }

    protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository) {
        account.withdraw(100L);
        // 더티체킹이 안되기 때문에!!
        if (accountRepository != null) {
            accountRepository.save(account);
        }
        Transaction transaction = Transaction.builder()
                .withdrawAccount(account)
                .depositAccount(null)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .amount(100L)
                .gubun(TransactionEnum.WITHDRAW)
                .sender(account.getNumber() + "")
                .receiver("ATM")
                .build();
        return transaction;
    }

    protected Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount,
                                                 AccountRepository accountRepository) {
        withdrawAccount.withdraw(100L);
        depositAccount.deposit(100L);
        // 더티체킹이 안되기 때문에!!
        if (accountRepository != null) {
            accountRepository.save(withdrawAccount);
            accountRepository.save(depositAccount);
        }

        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.TRANSFER)
                .sender(withdrawAccount.getNumber() + "")
                .receiver(depositAccount.getNumber() + "")
                .build();
        return transaction;
    }
}
