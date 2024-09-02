package bank.common.config.filter;

import bank.common.config.auth.LoginUser;
import bank.common.config.jwt.JwtProcess;
import bank.common.config.jwt.JwtUtil;
import bank.common.util.CustomResponseUtil;
import com.auth0.jwt.exceptions.JWTDecodeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

/**
 * 모든 주소에서 동작(토큰 검증)
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("JwtAuthorizationFilter.()");
        if (isHeaderVerify(request,response)){
            String token = request.getHeader(JwtUtil.Header).replace(JwtUtil.TOKEN_PREFIX, "");

            LoginUser loginUser = null;
            try {
                loginUser = JwtProcess.verify(token);
            }catch (JWTDecodeException e){
                CustomResponseUtil.unAuthentication(response,"토큰이 유효하지 않습니다.");
                return;
            }

            // 임시 세션 에는 UserDetails 타입이나 username을 담을수 있음
            // 핵심은 role을 잘 집어넣어
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }

    private boolean isHeaderVerify(HttpServletRequest request,HttpServletResponse response){
        String header = request.getHeader(JwtUtil.Header);
        if (header == null || !header.startsWith(JwtUtil.TOKEN_PREFIX)){
            return false;
        }
        return true;
    }
}
