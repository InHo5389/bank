package bank.controller.transaction;

import bank.common.config.auth.LoginUser;
import bank.controller.common.response.CustomApiResponse;
import bank.domain.transaction.dto.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface TransactionApiSpecification {

    @Operation(summary = "입금/출금 내역 보기", description = "입금,출금,입출금 버튼을 통해서 내역올 확인할수 있다. default로는 입출금 내역을 둘다 보여준다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "중복된 아이디입니다. 다른 아이디를 사용해 주세요.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "등록된 유저가 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "등록된 계좌가 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 계좌가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 하여 주세요.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "422", description = "출금 계좌의 잔액이 부족합니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class)))
    })
    CustomApiResponse<TransactionResponse.History> findTransactionList(Long number, String gubun, Integer page, LoginUser loginUser);
}
