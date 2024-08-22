package bank.common.config.filter;

import bank.common.config.dummy.DummyObject;
import bank.common.config.jwt.JwtUtil;
import bank.controller.user.dto.UserRequest;
import bank.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        userRepository.save(newUser("ssar","쌀"));
    }

    @Test
    @DisplayName("로그인에 성공하면 successfulAuthentication()를 탄다.")
    void successfulAuthentication() throws Exception {
        //given
        UserRequest.Login loginRequest = new UserRequest.Login("ssar","1234");
        String requestBody = om.writeValueAsString(loginRequest);
        System.out.println(requestBody);
        //when
        //then
        mockMvc.perform(post("/api/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.data.username").value("ssar"))
                .andReturn().getResponse().getHeader(JwtUtil.Header).startsWith(JwtUtil.TOKEN_PREFIX);

    }

    @Test
    @DisplayName("로그인의 실패하면 unSuccessfulAuthentication()를 탄다.")
    void unSuccessfulAuthentication() throws Exception {
        //given
        UserRequest.Login loginRequest = new UserRequest.Login("ssar","123");
        String requestBody = om.writeValueAsString(loginRequest);
        System.out.println(requestBody);
        //when
        //then
        mockMvc.perform(post("/api/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("로그인 실패"));
    }
}