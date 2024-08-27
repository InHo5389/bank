package bank.infrastructure.account;

import bank.domain.account.Account;
import bank.domain.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public Account save(Account account) {
        return accountJpaRepository.save(account);
    }

    @Override
    public Optional<Account> findByNumber(Long number) {
        return accountJpaRepository.findByNumber(number);
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        return accountJpaRepository.findByUserId(userId);
    }
}
