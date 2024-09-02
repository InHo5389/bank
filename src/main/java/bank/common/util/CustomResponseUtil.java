package bank.common.util;

import bank.controller.common.response.CustomApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void fail(HttpServletResponse response, String message, HttpStatus authException) throws IOException {
        try {
            CustomApiResponse<?> responseDto = new CustomApiResponse<>(authException,message,null);
            String responseBody = objectMapper.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(authException.value());
            response.getWriter().println(responseBody);
        }catch (Exception e){
            log.error("서버 파싱 에러");
        }
    }

    public static void success(HttpServletResponse response,Object dto) throws IOException {
        try {
            CustomApiResponse<?> responseDto = new CustomApiResponse<>(HttpStatus.OK,"로그인 성공",dto);
            String responseBody = objectMapper.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().println(responseBody);
        }catch (Exception e){
            log.error("서버 파싱 에러");
        }
    }

    public static void unAuthentication(HttpServletResponse response, String message) throws IOException {
        try {
            CustomApiResponse<?> responseDto = new CustomApiResponse<>(HttpStatus.UNAUTHORIZED,message,null);
            String responseBody = objectMapper.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().println(responseBody);
        }catch (Exception e){
            log.error("서버 파싱 에러");
        }
    }
}
