package bank.controller.account;

import bank.common.config.auth.LoginUser;
import bank.controller.account.dto.AccountRequest;
import bank.controller.common.response.CustomApiResponse;
import bank.domain.account.AccountService;
import bank.domain.account.dto.AccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/s/account")
    @Operation(summary = "계좌 생성", description = "계좌를 생성한다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse
    }
    )
    public CustomApiResponse<AccountResponse.Create> create(@Valid @RequestBody AccountRequest.Create request,
                                                            @AuthenticationPrincipal LoginUser loginUser) {
        AccountResponse.Create response = accountService.createAccount(request.toCommand(), loginUser.getUser().getId());
        return CustomApiResponse.ok(response);
    }

    @GetMapping("/s/account/login-user")
    public CustomApiResponse<AccountResponse.GetByUser> getByUser(@AuthenticationPrincipal LoginUser loginUser) {

        AccountResponse.GetByUser accounts = accountService.getByUser(loginUser.getUser().getId());
        return CustomApiResponse.ok(accounts);
    }

    @DeleteMapping("/s/account/{number}")
    public CustomApiResponse<Void> deleteAccount(@PathVariable Long number,
                                                 @AuthenticationPrincipal LoginUser loginUser) {
        accountService.delete(number, loginUser.getUser().getId());

        return CustomApiResponse.ok("계좌 삭제 완료", null);
    }

    @PostMapping("/account/deposit")
    public CustomApiResponse<AccountResponse.Deposit> deposit(@Valid @RequestBody AccountRequest.Deposit request) {
        return CustomApiResponse.ok("입금 완료", accountService.deposit(request.toCommand()));
    }

    @PostMapping("/s/account/withdraw")
    public CustomApiResponse<AccountResponse.Withdraw> withdraw(@Valid @RequestBody AccountRequest.Withdraw request,
                                                                @AuthenticationPrincipal LoginUser loginUser) {

        return CustomApiResponse.ok("출금 완료", accountService.withdraw(request.toCommand(), loginUser.getUser().getId()));
    }

    @PostMapping("/s/account/transfer")
    public CustomApiResponse<AccountResponse.Transfer> transfer(@Valid @RequestBody AccountRequest.Transfer request,
                                                                @AuthenticationPrincipal LoginUser loginUser) {
        return CustomApiResponse.ok("이체 완료", accountService.transfer(request.toCommand(), loginUser.getUser().getId()));
    }

    @GetMapping("/s/account/{number}")
    public CustomApiResponse<AccountResponse.Detail> findDetailAccount(
            @PathVariable Long accountNumber,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        AccountResponse.Detail response = accountService.getDetailAccount(accountNumber, loginUser.getUser().getId(), page);
        return CustomApiResponse.ok("계좌 상세 보기", response);
    }
}
