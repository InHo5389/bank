package bank.domain.user;

import java.util.Optional;

public interface UserRepository {

    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
}
