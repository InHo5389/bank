package bank.controller.account;

import bank.common.config.auth.LoginUser;
import bank.controller.account.dto.AccountRequest;
import bank.controller.common.response.ApiResponse;
import bank.domain.account.AccountService;
import bank.domain.account.dto.AccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/s/account")
    public ApiResponse<AccountResponse.Create> create(@Valid @RequestBody AccountRequest.Create request,
                                                      @AuthenticationPrincipal LoginUser loginUser){
        AccountResponse.Create response = accountService.createAccount(request.toCommand(), loginUser.getUser().getId());
        return ApiResponse.ok(response);
    }
}
