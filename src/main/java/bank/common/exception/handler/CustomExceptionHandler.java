package bank.common.exception.handler;

import bank.common.exception.CustomGlobalException;
import bank.controller.common.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomGlobalException.class)
    public ResponseEntity<?> apiExceptionHandler(CustomGlobalException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, ex.getMessage(),null), HttpStatus.BAD_REQUEST);
    }
}
