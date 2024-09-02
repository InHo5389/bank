package bank.controller.account;

import bank.common.config.auth.LoginUser;
import bank.controller.account.dto.AccountRequest;
import bank.controller.common.response.CustomApiResponse;
import bank.domain.account.dto.AccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface AccountApiSpecification {

    @Operation(summary = "계좌 생성", description = "계좌를 생성한다. 계좌번호와 계좌 비밀번호는 4자리만 사용가능하다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "등록된 유저가 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 계좌가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 하여 주세요.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class)))
    })
    CustomApiResponse<AccountResponse.Create> create(AccountRequest.Create request, LoginUser loginUser);

    @Operation(summary = "나의 계좌 조회", description = "나의 계좌들을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "등록된 유저가 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 계좌가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 하여 주세요.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class)))
    })
    CustomApiResponse<AccountResponse.GetByUser> getByUser(LoginUser loginUser);

    @Operation(summary = "계좌 삭제", description = "계좌를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "등록된 유저가 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 계좌가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 하여 주세요.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class)))
    })
    CustomApiResponse<Void> deleteAccount(Long number, LoginUser loginUser);

    @Operation(summary = "계좌 입금", description = "계좌의 돈을 입금한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "등록된 유저가 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 계좌가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 하여 주세요.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class)))
    })
    CustomApiResponse<AccountResponse.Deposit> deposit(AccountRequest.Deposit request);

    @Operation(summary = "계좌 출금", description = "계좌의 돈을 출금한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "등록된 유저가 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 계좌가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 하여 주세요.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "422", description = "출금 계좌의 잔액이 부족합니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class)))
    })
    CustomApiResponse<AccountResponse.Withdraw> withdraw(AccountRequest.Withdraw request, LoginUser loginUser);

    @Operation(summary = "계좌 이체", description = "다른 계좌로 돈을 이체한다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "등록된 유저가 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 계좌가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 하여 주세요.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class)))
    })
    CustomApiResponse<AccountResponse.Transfer> transfer(AccountRequest.Transfer request, LoginUser loginUser);

    @Operation(summary = "계좌 상세 조회", description = "해당 계좌에 대한 입출금 내역을 보여준다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "등록된 유저가 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 계좌가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 하여 주세요.",
                    content = @Content(schema = @Schema(implementation = CustomApiResponse.class)))
    })
    CustomApiResponse<AccountResponse.Detail> findDetailAccount(Long accountNumber, Integer page, LoginUser loginUser);
}
