package iuh.fit.se.servicelogin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserLogin {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
}