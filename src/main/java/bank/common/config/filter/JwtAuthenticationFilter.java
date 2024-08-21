package bank.common.config.filter;

import bank.common.config.auth.LoginUser;
import bank.common.config.jwt.JwtProcess;
import bank.common.config.jwt.JwtUtil;
import bank.common.util.CustomResponseUtil;
import bank.controller.user.dto.UserRequest;
import bank.domain.user.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
    }

    // post의 /login일때 동작
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.debug("디버그 : attemptAuthentication 호출됨");
        try {
            ObjectMapper om = new ObjectMapper();
            UserRequest.Login loginRequest = om.readValue(request.getInputStream(), UserRequest.Login.class);

            // 강제 로그인
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword());

            // UserDetailsService의 loadUserByUsername 호출
            // 강제 로그인 해주는 이유는 jwt를 쓴다 하더라도 컨트롤러 진입을 하면
            // 시큐리티 권한체크,인증체크 아래 코드의 도움을 받을수 있게 세션을 만든다.
            // 세션의 유효 기간은 request하고 response하면 끝
            // 다음 요청때는 세션을 못씀 jsessionID를 안넣어줘서
            //  http.authorizeHttpRequests(c->
            //                c.requestMatchers("/api/s/**").authenticated()

            return authenticationManager.authenticate(token);
        } catch (Exception e) {
            // 컨트롤러 어드바이스에 날릴수 없음 시큐리티 필터이기 때문에
            // 필터를 다 통과해야지 컨틀롤러 단으로 가는데 거기서 부터 exception이 터져야지
            // 컨트롤러 어드바이스로 날릴수 있음
            // throw를 하면 unsuccessfulAuthentication() 호출함
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // attemptAuthentication()가 정상적이면 해당 메서드 호출
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.debug("디버그 : successfulAuthentication 호출됨");
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        String jwtToken = JwtProcess.create(loginUser);
        response.addHeader(JwtUtil.Header, jwtToken);

        UserResponse.Login loginResponse = new UserResponse.Login(loginUser.getUser(),jwtToken);
        CustomResponseUtil.success(response,loginResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.unAuthentication(response,"로그인 실패");
    }
}

























