package iuh.fit.se.serviceregister.controller;

import iuh.fit.se.serviceregister.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService service;

    @PostMapping
    public String register(@RequestBody Map<String, String> req) {
        return service.register(req.get("username"), req.get("password"));
    }
}