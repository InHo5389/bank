package bank.domain.account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);
    Optional<Account> findByNumber(Long number);
    List<Account> findByUserId(Long userId);
    void deleteById(Long accountId);
}
