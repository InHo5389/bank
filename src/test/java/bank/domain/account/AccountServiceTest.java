package bank.domain.account;

import bank.common.config.dummy.DummyObject;
import bank.domain.account.dto.AccountCommand;
import bank.domain.account.dto.AccountResponse;
import bank.domain.common.exception.CustomGlobalException;
import bank.domain.transaction.Transaction;
import bank.domain.transaction.TransactionRepository;
import bank.domain.user.User;
import bank.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends DummyObject {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    @DisplayName("등록된 계자 번호가 없으면 계좌를 등록할 수 있다.")
    void createAccount() {
        //given
        long number = 1111L;
        AccountCommand.Create command = new AccountCommand.Create(number, 1234L);
        long userId = 1L;
        long accountId = 1L;
        User user = newMockUser(userId, "ssar", "쌀");
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user));
        when(accountRepository.findByNumber(number))
                .thenReturn(Optional.empty());
        when(accountRepository.save(any()))
                .thenReturn(newMockAccount(accountId, number, user, 1000L));
        //when
        //then
        AccountResponse.Create account = accountService.createAccount(command, userId);
        Assertions.assertThat(account)
                .extracting("accountId", "number")
                .containsExactlyInAnyOrder(accountId, number);
    }

    @Test
    @DisplayName("로그인된 유저 id에 대한 계좌 목록을 보여준다.")
    void getByUser() {
        //given
        long userId = 1L;
        User user = newMockUser(userId, "inho", "핸드폰");
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        List<Account> accountList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            accountList.add(newMockAccount(1L, Long.valueOf("111" + i), user, 1000L));
        }
        when(accountRepository.findByUserId(userId))
                .thenReturn(accountList);
        //when
        AccountResponse.GetByUser accountListByUser = accountService.getByUser(userId);
        //then
        Assertions.assertThat(accountListByUser)
                .extracting("userId", "fullname")
                .containsExactly(1L, "핸드폰");

        List<AccountResponse.GetByUser.AccountDto> accountDto = accountListByUser.getAccounts();

        Assertions.assertThat(accountDto)
                .hasSize(5)
                .extracting("accountId", "number", "balance")
                .containsExactly(
                        Tuple.tuple(1L, 1110L, 1000L),
                        Tuple.tuple(1L, 1111L, 1000L),
                        Tuple.tuple(1L, 1112L, 1000L),
                        Tuple.tuple(1L, 1113L, 1000L),
                        Tuple.tuple(1L, 1114L, 1000L)
                );
    }

    @Test
    @DisplayName("계좌 확인시 소유자가 아닐 경우 예외가 발생한다.")
    void delete() {
        //given
        long userId = 1L;
        long number = 1111L;

        User user = newMockUser(2L, "ssar", "쌀");
        Account account = newMockAccount(1L, number, user, 1000L);
        when(accountRepository.findByNumber(account.getNumber()))
                .thenReturn(Optional.of(account));
        //when
        Assertions.assertThatThrownBy(() -> accountService.delete(number, userId))
                .hasMessage("해당 계좌 소유자가 아닙니다.")
                .isInstanceOf(CustomGlobalException.class);
        //then
    }

    @Test
    @DisplayName("ATM으로 입금을 할 수 있다.")
    void deposit() {
        //given
        long number = 1111L;
        long amount = 100L;
        String receiver = "이노";
        String tel = "010-1234-5678";

        AccountCommand.Deposit command = new AccountCommand.Deposit(
                number, amount, "DEPOSIT", "010-1234-5678");
        Account account = newMockAccount(1L, number, newMockUser(1L, "ssar", "쌀"), 1000L);
        when(accountRepository.findByNumber(command.getNumber()))
                .thenReturn(Optional.of(account));

        Account account1 = newMockAccount(1L, number, newMockUser(1L, "ssar", "쌀"), 1000L);
        Transaction transaction = newMockDepositTransaction(1L, account1, amount, receiver, tel);
        when(transactionRepository.save(any())).thenReturn(transaction);
        //when
        AccountResponse.Deposit response = accountService.deposit(command);
        //then
        Assertions.assertThat(account.getBalance()).isEqualTo(1100L);
        Assertions.assertThat(transaction.getDepositAccountBalance()).isEqualTo(1100L);
        Assertions.assertThat(response)
                .extracting("accountId", "number", "transaction.amount")
                .containsExactly(1L, number, amount);
    }

    @Test
    @DisplayName("")
    void withdraw() {
        //given
        long userId = 1L;
        long number = 1111L;
        long password = 1111L;
        long amount = 100L;

        AccountCommand.Withdraw command = new AccountCommand.Withdraw(
                number, password, amount, "WITHDRAW");
        User user = newMockUser(1L, "ssar", "쌀");
        Account account = Account.builder().id(1L).user(user).number(number).password(password).balance(1000L).build();
        when(accountRepository.findByNumber(command.getNumber()))
                .thenReturn(Optional.of(account));

        Account account2 = Account.builder().id(1L).user(user).number(number).password(password).balance(1000L).build();

        Transaction transaction = newMockWithdrawTransaction(1L, account2, amount);
        when(transactionRepository.save(any())).thenReturn(transaction);
        //when
        AccountResponse.Withdraw response = accountService.withdraw(command, userId);
        //then
        Assertions.assertThat(account.getBalance()).isEqualTo(900L);
        System.out.println(transaction.getDepositAccountBalance());
        Assertions.assertThat(transaction.getWithdrawAccountBalance()).isEqualTo(900L);
        Assertions.assertThat(response)
                .extracting("accountId", "number", "transaction.amount")
                .containsExactly(1L, number, amount);
    }

    @Test
    @DisplayName("계자이체를 할수 있다.")
    void transfer() {
        //given
        long userId = 1L;
        long withdrawNumber = 1111L;
        long depositNumber = 1112L;
        long withdrawPassword = 1234L;
        long depositPassword = 4321L;
        long amount = 100L;

        AccountCommand.Transfer command = new AccountCommand.Transfer(
                withdrawNumber,depositNumber, withdrawPassword, amount, "TRANSFER");

        User user1 = newMockUser(2L, "ssar", "쌀");
        Account depositAccount = Account.builder().id(1L).user(user1).number(depositNumber).password(depositPassword).balance(1000L).build();
        User user2 = newMockUser(1L, "cos", "코스");
        Account withdrawAccount = Account.builder().id(2L).user(user2).number(withdrawNumber).password(withdrawPassword).balance(1000L).build();
        when(accountRepository.findByNumber(command.getWithdrawNumber()))
                .thenReturn(Optional.of(withdrawAccount));
        when(accountRepository.findByNumber(command.getDepositNumber()))
                .thenReturn(Optional.of(depositAccount));


        Account depositAccount2 = Account.builder().id(2L).user(user1).number(depositNumber).password(depositPassword).balance(1000L).build();
        Account withdrawAccount2 = Account.builder().id(1L).user(user2).number(withdrawNumber).password(withdrawPassword).balance(1000L).build();

        Transaction transaction = newMockTransferTransaction(1L,withdrawAccount2,depositAccount2, amount);
        when(transactionRepository.save(any())).thenReturn(transaction);
        //when
        AccountResponse.Transfer response = accountService.transfer(command, userId);
        //then
        Assertions.assertThat(withdrawAccount2.getBalance()).isEqualTo(900L);
        Assertions.assertThat(depositAccount2.getBalance()).isEqualTo(1100L);
        Assertions.assertThat(transaction.getWithdrawAccountBalance()).isEqualTo(900L);
        Assertions.assertThat(response)
                .extracting("accountId", "number", "transaction.amount")
                .containsExactly(2L, withdrawNumber, amount);
    }

}