package iuh.fit.se.servicelogin.controller;

import iuh.fit.se.servicelogin.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> req) {
        return Map.of("token",
                service.login(req.get("username"), req.get("password"))
        );
    }
}