package bank.controller.account;

import bank.common.config.dummy.DummyObject;
import bank.controller.account.dto.AccountRequest;
import bank.domain.account.AccountService;
import bank.domain.account.dto.AccountCommand;
import bank.domain.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest extends DummyObject {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        userRepository.save(newUser("ssar","쌀"));
    }

    // jwt 토큰을 넣어줄 필요가 없다. JwtAuthorization 필터에서 토큰이 없더라도 통과되는데
    // 결국 시큐ꈰ티 단에서 세션 값 겁증에 실패해서 예외 메시지가 나온다
    // 시큐리티 컨피그에 설정한 예외핸들링 값이
    // 결국 시큐리티 세션만 생성을 해주면 된다. (jwt token -> 인증 필터 -> 시큐리티 세션 생성)
    // db에서 usename=ssar을 조회 하여 세션에 담아주는 어노테이션
    // setupBefore=TEST_METHOD(setUp메서드 실행전에 수행됨)
    // TEST_EXECUTION(create() 메서드 실행전에 수행)
    @WithUserDetails(value = "ssar",setupBefore = TestExecutionEvent.TEST_EXECUTION)
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

}