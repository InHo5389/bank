package bank.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    DUPLICATE_USERNAME(400, "중복된 아이디입니다. 다른 아이디를 사용해 주세요."),
    NOT_FOUND_USERNAME(400,"등록된 유저가 없습니다.");
    private final int status;
    private final String message;
}
