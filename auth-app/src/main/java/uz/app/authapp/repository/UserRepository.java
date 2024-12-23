package uz.app.authapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.authapp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
