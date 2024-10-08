package bank.controller.account;

import bank.common.config.dummy.DummyObject;
import bank.controller.account.dto.AccountRequest;
import bank.domain.account.Account;
import bank.domain.account.AccountRepository;
import bank.domain.account.dto.AccountCommand;
import bank.domain.user.User;
import bank.domain.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerIntergrationTest extends DummyObject {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        User ssar = userRepository.save(newUser("ssar", "쌀"));
        User cos = userRepository.save(newUser("cos", "코스"));
        accountRepository.save(newAccount(1111L, ssar));
        accountRepository.save(newAccount(2222L, cos));
        em.clear();
    }

    // jwt 토큰을 넣어줄 필요가 없다. JwtAuthorization 필터에서 토큰이 없더라도 통과되는데
    // 결국 시큐ꈰ티 단에서 세션 값 겁증에 실패해서 예외 메시지가 나온다
    // 시큐리티 컨피그에 설정한 예외핸들링 값이
    // 결국 시큐리티 세션만 생성을 해주면 된다. (jwt token -> 인증 필터 -> 시큐리티 세션 생성)
    // db에서 usename=ssar을 조회 하여 세션에 담아주는 어노테이션
    // setupBefore=TEST_METHOD(setUp메서드 실행전에 수행됨)
    // TEST_EXECUTION(create() 메서드 실행전에 수행)
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("계좌를 등록할수 있다.")
    void create() throws Exception {
        //given
        long number = 9999L;
        AccountRequest.Create request = new AccountRequest.Create(number, 1234L);
        String requestBody = objectMapper.writeValueAsString(request);
        System.out.println(requestBody);
        //when
        //then
        mockMvc.perform(post("/api/s/account")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.number").value(number));
    }

    @Test
    @DisplayName("본인 계좌 목록을 보여준다.")
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void test() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/s/account/login-user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.fullname").value("쌀"))
                .andExpect(jsonPath("$.data.accounts").isArray());
    }

    @Test
    @DisplayName("본인 계좌를 삭제한다.")
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteAccount() throws Exception {
        //given
        Long number = 1111L;
        //when
        //then
        mockMvc.perform(delete("/api/s/account/{number}", number))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("계좌 삭제 완료"));
    }

    @Test
    @DisplayName("본인 계좌를 삭제할때 계자 소유자가 아니면 계좌를 삭제하지 못하고 예외가 발생한다.")
    @WithUserDetails(value = "cos", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteAccount_fail() throws Exception {
        //given
        Long number = 1111L;
        //when
        //then
        mockMvc.perform(delete("/api/s/account/{number}", number))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 계좌 소유자가 아닙니다."));
    }

    @Test
    @DisplayName("")
    void deposit() throws Exception {
        //given
        long number = 1111L;
        long amount = 100L;

        AccountRequest.Deposit request = new AccountRequest.Deposit(
                number, amount, "DEPOSIT", "010-1234-5678");
        String requestBody = objectMapper.writeValueAsString(request);
        //when
        //then
        mockMvc.perform(post("/api/account/deposit")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("입금 완료"));
    }

    @Test
    @DisplayName("출금 계좌에 돈이 넉넉할 경우 출금을 할 수 있다.")
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void withdraw() throws Exception {
        //given
        long number = 1111L;
        long password = 1234L;
        long amount = 100L;

        AccountRequest.Withdraw request = new AccountRequest.Withdraw(
                number, password, amount, "WITHDRAW");
        String requestBody = objectMapper.writeValueAsString(request);
        //when
        //then
        mockMvc.perform(post("/api/s/account/withdraw")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("출금 완료"));
    }

    @Test
    @DisplayName("출금 계좌에 돈이 넉넉할 경우 이체를 할 수 있다.")
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void transfer() throws Exception {
        //given
        long withdrawNumber = 1111L;
        long depositNumber = 2222L;
        long withdrawPassword = 1234L;
        long amount = 100L;

        AccountRequest.Transfer request = new AccountRequest.Transfer(
                withdrawNumber, depositNumber, withdrawPassword, amount, "TRANSFER");
        String requestBody = objectMapper.writeValueAsString(request);
        //when
        //then

        mockMvc.perform(post("/api/s/account/transfer")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("이체 완료"));
    }

}