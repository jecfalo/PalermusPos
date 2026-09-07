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
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager manager;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authentication(@RequestBody AuthenticatedUser auth){
        Authentication authToken = new UsernamePasswordAuthenticationToken(auth.username(), auth.password());
        Authentication userAuth = manager.authenticate(authToken);
        User user = (User) userAuth.getPrincipal();

        String accessToken = tokenService.generateToken(user);
        
        ResponseCookie cookie = ResponseCookie.from("auth_token", accessToken)
                .httpOnly(true)
                .secure(false) // Cambiar a true en producción con HTTPS
                .path("/")
                .maxAge(2 * 60 * 60)
                .build();

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("username", user.getUsername());
        responseBody.put("role", user.getProfile().getUserType().name());
        responseBody.put("message", "Login exitoso");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(responseBody);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        ResponseCookie cookie = ResponseCookie.from("auth_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0) // Borrar la cookie
                .build();
                
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logout exitoso");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(responseBody);
    }

}
