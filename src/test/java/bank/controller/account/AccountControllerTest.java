package bank.controller.account;

import bank.common.config.SecurityConfig;
import bank.common.config.dummy.DummyObject;
import bank.controller.account.dto.AccountRequest;
import bank.domain.account.Account;
import bank.domain.account.AccountService;
import bank.domain.account.dto.AccountCommand;
import bank.domain.account.dto.AccountResponse;
import bank.domain.common.exception.CustomGlobalException;
import bank.domain.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest extends DummyObject {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(post("/**").with(csrf()))
                .defaultRequest(put("/**").with(csrf()))
                .defaultRequest(delete("/**").with(csrf()))
                .build();
    }

    @Test
    @WithUserDetails(value = "ssar",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("계좌를 등록할수 있다.")
    void create() throws Exception {
        // given
        long number = 9999L;
        AccountRequest.Create request = new AccountRequest.Create(number, 1234L);
        String requestBody = objectMapper.writeValueAsString(request);

        // 서비스 모킹
        given(accountService.createAccount(any(AccountCommand.Create.class), anyLong()))
                .willReturn(new AccountResponse.Create(newMockAccount(1L, number, newMockUser(1L, "ssar", "쌀"), 1000L)));

        // when & then
        mockMvc.perform(post("/api/s/account")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.number").value(number));
    }

    @Test
    @WithUserDetails(value = "ssar",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("본인 계좌 목록을 보여준다.")
    void getAccounts() throws Exception {
        // given
        User user = newMockUser(1L, "ssar", "쌀");
        List<Account> accountList = List.of(
                newMockAccount(1L, 1111L, user, 1000L),
                newMockAccount(2L, 1112L, user, 1000L),
                newMockAccount(3L, 1113L, user, 1000L),
                newMockAccount(4L, 1114L, user, 1000L)
        );

        AccountResponse.GetByUser response = new AccountResponse.GetByUser(user, accountList);

        given(accountService.getByUser(anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/s/account/login-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.fullname").value("쌀"))
                .andExpect(jsonPath("$.data.accounts").isArray());

    }

    @Test
    @WithUserDetails(value = "ssar",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("본인 계좌를 삭제한다.")
    void deleteAccount() throws Exception {
        // given
        Long number = 1111L;

        doNothing().when(accountService).delete(eq(number), anyLong());

        // when & then
        mockMvc.perform(delete("/api/s/account/{number}", number))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("계좌 삭제 완료"));

        verify(accountService).delete(eq(number), anyLong());
    }

    @Test
    @DisplayName("본인 계좌를 삭제할때 계자 소유자가 아니면 계좌를 삭제하지 못하고 예외가 발생한다.")
    @WithUserDetails(value = "ssar",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteAccount_fail() throws Exception {
        // given
        Long number = 1111L;

        // when & then
        mockMvc.perform(delete("/api/s/account/{number}", number))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 계좌 소유자가 아닙니다."));
    }
}