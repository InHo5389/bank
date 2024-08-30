package bank.controller.transaction;

import bank.common.config.auth.LoginUser;
import bank.controller.common.response.ApiResponse;
import bank.domain.transaction.TransactionService;
import bank.domain.transaction.dto.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/s/account/{number}/transaction")
    public ApiResponse<TransactionResponse.History> findTransactionList(
            @PathVariable Long number,
            @RequestParam(value = "gubun", defaultValue = "ALL") String gubun,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @AuthenticationPrincipal LoginUser loginUser) {

        TransactionResponse.History response = transactionService.getTransactionList(loginUser.getUser().getId(), number, gubun, page);
        return ApiResponse.ok("입출금 목록 보기",response);
    }
}
