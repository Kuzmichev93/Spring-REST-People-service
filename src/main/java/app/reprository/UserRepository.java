package app.reprository;

import app.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<UserAuth, Integer> {
    UserAuth findByUsername(String username);
    boolean existsByUsername(String username);
}
