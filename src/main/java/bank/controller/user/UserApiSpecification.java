package bank.controller.user;

import bank.controller.common.response.CustomApiResponse;
import bank.controller.user.dto.UserRequest;
import bank.domain.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface UserApiSpecification {

    @Operation(summary = "회원가입", description = "회원 가입을 한다. 회원 가입 시 아이디는 중복이 되지 않는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "중복된 아이디입니다. 다른 아이디를 사용해 주세요.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
    })
    CustomApiResponse<UserResponse.Join> join(UserRequest.Join request);
}
