package bank.infrastructure.account;

import bank.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<Account,Long> {
}
