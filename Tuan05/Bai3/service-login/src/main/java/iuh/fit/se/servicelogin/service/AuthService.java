package iuh.fit.se.servicelogin.service;

import iuh.fit.se.servicelogin.config.JwtUtil;
import iuh.fit.se.servicelogin.entity.UserLogin;
import iuh.fit.se.servicelogin.repository.LoginRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final LoginRepo repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public String login(String username, String password) {
        UserLogin user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return jwt.generateToken(username);
    }
}