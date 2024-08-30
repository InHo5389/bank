package bank.controller.account;

import bank.common.config.auth.LoginUser;
import bank.controller.account.dto.AccountRequest;
import bank.controller.common.response.ApiResponse;
import bank.domain.account.AccountService;
import bank.domain.account.dto.AccountResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/s/account")
    public ApiResponse<AccountResponse.Create> create(@Valid @RequestBody AccountRequest.Create request,
                                                      @AuthenticationPrincipal LoginUser loginUser) {
        AccountResponse.Create response = accountService.createAccount(request.toCommand(), loginUser.getUser().getId());
        return ApiResponse.ok(response);
    }

    @GetMapping("/s/account/login-user")
    public ApiResponse<AccountResponse.GetByUser> getByUser(@AuthenticationPrincipal LoginUser loginUser) {

        AccountResponse.GetByUser accounts = accountService.getByUser(loginUser.getUser().getId());
        return ApiResponse.ok(accounts);
    }

    @DeleteMapping("/s/account/{number}")
    public ApiResponse<Void> deleteAccount(@PathVariable Long number,
                                           @AuthenticationPrincipal LoginUser loginUser) {
        accountService.delete(number, loginUser.getUser().getId());

        return ApiResponse.ok("계좌 삭제 완료", null);
    }

    @PostMapping("/account/deposit")
    public ApiResponse<AccountResponse.Deposit> deposit(@Valid @RequestBody AccountRequest.Deposit request) {
        return ApiResponse.ok("입금 완료", accountService.deposit(request.toCommand()));
    }

    @PostMapping("/s/account/withdraw")
    public ApiResponse<AccountResponse.Withdraw> withdraw(@Valid @RequestBody AccountRequest.Withdraw request,
                                                          @AuthenticationPrincipal LoginUser loginUser) {

        return ApiResponse.ok("출금 완료", accountService.withdraw(request.toCommand(),loginUser.getUser().getId()));
    }

    @PostMapping("/s/account/transfer")
    public ApiResponse<AccountResponse.Transfer> transfer(@Valid @RequestBody AccountRequest.Transfer request,
                                                          @AuthenticationPrincipal LoginUser loginUser){
        return ApiResponse.ok("이체 완료",accountService.transfer(request.toCommand(),loginUser.getUser().getId()));
    }

    @GetMapping("/s/account/{number}")
    public ApiResponse<AccountResponse.Detail> findDetailAccount(
            @PathVariable Long accountNumber,
            @RequestParam(value = "page",defaultValue = "0") Integer page,
            @AuthenticationPrincipal LoginUser loginUser
    ){
        AccountResponse.Detail response = accountService.getDetailAccount(accountNumber, loginUser.getUser().getId(), page);
        return ApiResponse.ok("계좌 상세 보기",response);
    }
}
