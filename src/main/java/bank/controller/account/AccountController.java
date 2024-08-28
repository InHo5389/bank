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

        return ApiResponse.ok("계좌 삭제 완료",null);
    }

    @PostMapping("/account/deposit")
    public ApiResponse<AccountResponse.Deposit> deposit(@Valid @RequestBody AccountRequest.Deposit request){
        return ApiResponse.ok("입금 완료",accountService.deposit(request.toCommand()));
    }
}
