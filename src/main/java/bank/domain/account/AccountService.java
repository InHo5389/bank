package bank.domain.account;

import bank.domain.account.dto.AccountCommand;
import bank.domain.account.dto.AccountResponse;
import bank.domain.common.exception.CustomGlobalException;
import bank.domain.common.exception.ErrorType;
import bank.domain.transaction.Transaction;
import bank.domain.transaction.TransactionEnum;
import bank.domain.transaction.TransactionRepository;
import bank.domain.user.User;
import bank.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountResponse.Create createAccount(AccountCommand.Create command, Long userId) {
        // 유저 로그인 되어 있는 상태 이건 서비스 책임이 아니라 컨트롤러 책임
        // 유저가 db에 있는지 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
        // 해당 계좌가 db에 있는 중복여부 체크
        Optional<Account> optionalAccount = accountRepository.findByNumber(command.getNumber());
        if (optionalAccount.isPresent()) {
            throw new CustomGlobalException(ErrorType.DUPLICATE_ACCOUNT_NUMBER);
        }
        // 계좌 등록
        Account account = accountRepository.save(command.toEntity(user));
        // dto 응답
        return new AccountResponse.Create(account);
    }

    public AccountResponse.GetByUser getByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
        List<Account> accountList = accountRepository.findByUserId(userId);
        return new AccountResponse.GetByUser(user, accountList);
    }

    @Transactional
    public void delete(Long number, Long userId) {
        Account account = accountRepository.findByNumber(number)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_ACCOUNT));
        account.checkOwner(userId);
        accountRepository.deleteById(account.getId());
    }

    @Transactional
    public AccountResponse.Deposit deposit(AccountCommand.Deposit command) {
        Account account = accountRepository.findByNumber(command.getNumber())
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_ACCOUNT));

        account.deposit(command.getAmount());

        Transaction transaction = Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .amount(command.getAmount())
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber() + "")
                .tel(command.getTel())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return new AccountResponse.Deposit(account, savedTransaction);
    }

    @Transactional
    public AccountResponse.Withdraw withdraw(AccountCommand.Withdraw command, Long userId) {
        Account withdrawAccount = accountRepository.findByNumber(command.getNumber())
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_ACCOUNT));
        // 출금 소유자 확인(로그인한 사람과 동일한지)
        withdrawAccount.checkOwner(userId);
        withdrawAccount.checkPassword(command.getPassword());
        withdrawAccount.withdraw(command.getAmount());

        // 내 계좌에서 ATM으로 출금(돈 뽑기)
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(null)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(null)
                .amount(command.getAmount())
                .gubun(TransactionEnum.WITHDRAW)
                .sender(withdrawAccount.getNumber()+"")
                .receiver("ATM")
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new AccountResponse.Withdraw(withdrawAccount,savedTransaction);
    }
}
