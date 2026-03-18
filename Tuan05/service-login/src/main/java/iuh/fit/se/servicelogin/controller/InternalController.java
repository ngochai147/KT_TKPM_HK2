package iuh.fit.se.servicelogin.controller;

import iuh.fit.se.servicelogin.entity.UserLogin;
import iuh.fit.se.servicelogin.repository.LoginRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {

    private final LoginRepo repo;

    @PostMapping("/users")
    public void syncUser(@RequestBody UserLogin user) {
        repo.save(user);
    }
}