package com.estore.authenticationauthorizationservice.authentication;

import com.estore.authenticationauthorizationservice.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("oauth/")
public class AuthenticationController {

    private final PasswordEncoder passwordEncoder;

    @GetMapping("me")
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<AuthenticationUser> getUser(@AuthenticatedLoggedInUser AuthenticationUser currentUser) {
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("encode")
    public String login(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }
}
