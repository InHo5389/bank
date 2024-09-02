package bank.domain.transaction;

import bank.domain.account.Account;
import bank.domain.account.AccountRepository;
import bank.domain.common.exception.CustomGlobalException;
import bank.domain.common.exception.ErrorType;
import bank.domain.transaction.dto.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionResponse.History getTransactionList(Long userId, Long accountNumber, String gubun,
                                                  int page) {

        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_ACCOUNT));
        account.checkOwner(userId);

        List<Transaction> transactionList = transactionRepository.findTransactionList(account.getId(),gubun,page);
        return new TransactionResponse.History(account, transactionList);
    }
}
