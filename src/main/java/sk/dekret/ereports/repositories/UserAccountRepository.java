package sk.dekret.ereports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import sk.dekret.ereports.db.entities.UserAccount;

import java.util.Optional;

@Component
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsername(String username);
}
