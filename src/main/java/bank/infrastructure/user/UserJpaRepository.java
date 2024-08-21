package bank.infrastructure.user;

import bank.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
}
