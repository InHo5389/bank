package bank.infrastructure.account;

import bank.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByNumber(Long number);
    List<Account> findByUserId(Long userId);
}
