package com.jecfalo.palermus_api.modules.users.controllers;

import com.jecfalo.palermus_api.core.config.JwtToken;
import com.jecfalo.palermus_api.core.security.TokenService;
import com.jecfalo.palermus_api.modules.users.models.User;
import com.jecfalo.palermus_api.modules.users.records.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager manager;

    @PostMapping("/login")
    public ResponseEntity<JwtToken> authentication(@RequestBody AuthenticatedUser auth){
        Authentication authToken = new UsernamePasswordAuthenticationToken(auth.username(), auth.password());
        Authentication userAuth = manager.authenticate(authToken);
        User user = (User) userAuth.getPrincipal();

        String accessToken = tokenService.generateToken(user);

        return ResponseEntity.ok(new JwtToken(accessToken));
    }

}
