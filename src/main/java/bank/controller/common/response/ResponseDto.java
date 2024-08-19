package bank.controller.common.response;

/**
 * @param code 1성공 , -1 실패
 */
public record ResponseDto<T>(Integer code, String msg, T data) {
}
