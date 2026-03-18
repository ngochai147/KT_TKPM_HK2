package iuh.fit.se.servicelogin.repository;

import iuh.fit.se.servicelogin.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepo extends JpaRepository<UserLogin, Long> {
    Optional<UserLogin> findByUsername(String username);
}