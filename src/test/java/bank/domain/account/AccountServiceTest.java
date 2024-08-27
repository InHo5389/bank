package bank.domain.account;

import bank.common.config.dummy.DummyObject;
import bank.controller.account.dto.AccountRequest;
import bank.domain.account.dto.AccountCommand;
import bank.domain.account.dto.AccountResponse;
import bank.domain.user.User;
import bank.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

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
    void createAccount(){
        //given
        long number = 1111L;
        AccountCommand.Create command = new AccountCommand.Create(number,1234L);
        long userId = 1L;
        long accountId= 1L;
        User user = newMockUser(userId, "ssar", "쌀");
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user));
        when(accountRepository.findByNumber(number))
                .thenReturn(Optional.empty());
        when(accountRepository.save(any()))
                .thenReturn(newMockAccount(accountId,number,user,1000L));
        //when
        //then
        AccountResponse.Create account = accountService.createAccount(command, userId);
        Assertions.assertThat(account)
                .extracting("accountId", "number")
                .containsExactlyInAnyOrder(accountId,number);
    }

}