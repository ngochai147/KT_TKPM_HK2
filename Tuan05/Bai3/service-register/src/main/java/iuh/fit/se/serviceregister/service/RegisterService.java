package iuh.fit.se.serviceregister.service;

import iuh.fit.se.serviceregister.entity.UserRegister;
import iuh.fit.se.serviceregister.repository.RegisterRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final RegisterRepo repo;
    private final RestTemplate restTemplate;
    private final PasswordEncoder encoder;

    private final String LOGIN_SERVICE = "http://localhost:8081/internal/users";

    public String register(String username, String password) {

        UserRegister user = new UserRegister();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        repo.save(user);

        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", user.getPassword());

        restTemplate.postForObject(LOGIN_SERVICE, body, Object.class);

        return "Register success + synced";
    }
}