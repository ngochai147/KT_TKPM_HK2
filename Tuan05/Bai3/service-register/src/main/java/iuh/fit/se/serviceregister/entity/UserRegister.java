package iuh.fit.se.serviceregister.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserRegister {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
}