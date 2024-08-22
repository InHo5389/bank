package bank.common.config.filter;

import bank.common.config.auth.LoginUser;
import bank.common.config.jwt.JwtProcess;
import bank.common.config.jwt.JwtUtil;
import bank.domain.user.User;
import bank.domain.user.UserEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("인증이 필요한 url에 유효한 token을 헤더로 보내고 없는 url일 경우 404가 뜬다.")
    void JwtAuthorization_success() throws Exception {
        //given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);
        String token = JwtProcess.create(loginUser);
        //when
        //then
        mockMvc.perform(get("/api/s/hello")
                        .header(JwtUtil.Header, token))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("token을 헤더로 보내지 않으면 401이 뜬다.")
    void JwtAuthorization_fail() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/s/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("admin권한이 필요한 url에 customer로 요청하면 403이 뜬다.")
    void JwtAuthorization_authorization_fail() throws Exception {
        //given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);
        String token = JwtProcess.create(loginUser);
        //when
        //then
        mockMvc.perform(get("/api/admin/hello")
                        .header(JwtUtil.Header, token))
                .andExpect(status().isForbidden());
    }

}