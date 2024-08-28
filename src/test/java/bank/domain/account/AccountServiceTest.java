package bank.domain.account;

import bank.common.config.dummy.DummyObject;
import bank.controller.account.dto.AccountRequest;
import bank.domain.account.dto.AccountCommand;
import bank.domain.account.dto.AccountResponse;
import bank.domain.common.exception.CustomGlobalException;
import bank.domain.user.User;
import bank.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

}