package bank.common.util;

import bank.controller.common.response.ApiResponse;
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
            ApiResponse<?> responseDto = new ApiResponse<>(authException,message,null);
            String responseBody = objectMapper.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(authException.value());
            response.getWriter().println(responseBody);
        }catch (Exception e){
            log.error("서버 파싱 에러");
        }
    }
}
