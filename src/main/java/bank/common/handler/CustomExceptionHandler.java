package bank.common.handler;

import bank.domain.common.exception.CustomGlobalException;
import bank.controller.common.response.ResponseDto;
import bank.domain.common.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    // 의도된 에러 캐치
    @ExceptionHandler(CustomGlobalException.class)
    public ResponseEntity<?> apiExceptionHandler(CustomGlobalException ex) {
        ErrorType errorType = ex.getErrorType();
        log.warn("ErrorType: {}, Message: {}", errorType, ex.getMessage());
        int status = errorType.getStatus();
        return new ResponseEntity<>(
                new ResponseDto<>(status, ex.getMessage(), null),
                HttpStatus.valueOf(status));
    }
}
