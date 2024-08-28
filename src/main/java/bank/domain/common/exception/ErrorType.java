package bank.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    DUPLICATE_USERNAME(400, "중복된 아이디입니다. 다른 아이디를 사용해 주세요."),
    NOT_FOUND_USER(400,"등록된 유저가 없습니다."),
    NOT_FOUND_ACCOUNT(400,"등록된 계좌가 없습니다."),
    INVALID_ACCOUNT_OWNER(400,"해당 계좌 소유자가 아닙니다."),
    DUPLICATE_ACCOUNT_NUMBER(400,"해당 계좌는 이미 존재합니다. 다른 계좌번호를 사용하여 주세요");
    private final int status;
    private final String message;
}
