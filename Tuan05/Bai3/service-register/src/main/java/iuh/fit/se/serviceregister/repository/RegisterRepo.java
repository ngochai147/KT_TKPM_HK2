package iuh.fit.se.serviceregister.repository;

import iuh.fit.se.serviceregister.entity.UserRegister;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepo extends JpaRepository<UserRegister, Long> {
}