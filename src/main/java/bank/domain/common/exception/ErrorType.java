package bank.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    DUPLICATE_USERNAME(409, "중복된 아이디입니다. 다른 아이디를 사용해 주세요."),
    NOT_FOUND_USER(404, "등록된 유저가 없습니다."),
    NOT_FOUND_ACCOUNT(404, "등록된 계좌가 없습니다."),
    NOT_FOUND_DEPOSIT_ACCOUNT(404, "등록된 계좌가 없습니다."),
    NOT_FOUND_WITHDRAW_ACCOUNT(404, "등록된 계좌가 없습니다."),
    INVALID_USER_PASSWORD(401, "비밀번호값이 틀립니다. 다시 시도하여주세요."),
    INVALID_ACCOUNT_OWNER(403, "해당 계좌 소유자가 아닙니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    NOT_ENOUGH_BALANCE(422, "출금 계좌의 잔액이 부족합니다."),
    SAME_ACCOUNT_NUMBER(400, "입출금 계좌가 동일할 수 없습니다."),
    DUPLICATE_ACCOUNT_NUMBER(409, "해당 계좌는 이미 존재합니다. 다른 계좌번호를 사용하여 주세요");
    private final int status;
    private final String message;
}
