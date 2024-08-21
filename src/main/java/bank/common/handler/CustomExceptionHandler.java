package bank.common.handler;

import bank.controller.common.response.ApiResponse;
import bank.domain.common.exception.CustomGlobalException;
import bank.domain.common.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    // 의도된 에러 캐치
    @ExceptionHandler(CustomGlobalException.class)
    public ResponseEntity<?> apiExceptionHandler(CustomGlobalException e) {
        ErrorType errorType = e.getErrorType();
        log.warn("ErrorType: {}, Message: {}", errorType, e.getMessage());
        HttpStatus httpStatus = HttpStatus.valueOf(errorType.getStatus());
        return new ResponseEntity<>(
                new ApiResponse<>(httpStatus, e.getMessage(), null),
                httpStatus);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, String> errorMap = fieldErrors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "알 수 없는 오류",
                        (existing, replacement) -> existing
                ));
        return ApiResponse.of(HttpStatus.BAD_REQUEST,"바인딩 오류", errorMap);
    }
}
