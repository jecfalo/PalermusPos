package com.jecfalo.palermus_api.core.security;

import com.jecfalo.palermus_api.modules.users.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository repository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String tokenJWT = extractToken(request);
            if(tokenJWT != null){
                String subject = tokenService.getSecret(tokenJWT);
                if(subject != null){
                    UserDetails user = repository.findByUsername(subject);
                    if(user != null){
                        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("Autenticacion exitosa en SecurityFilter para el usuario: " + subject);
                        System.out.println("Roles del usuario: " + user.getAuthorities());
                    } else {
                        System.out.println("Usuario no encontrado en la BD: " + subject);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error en SecurityFilter: " + e.getMessage());
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }
    private String extractToken(HttpServletRequest request){
        String headerAuthorization = request.getHeader("Authorization");
        if(headerAuthorization != null){
            return headerAuthorization.replace("Bearer ", "");
        }
        return null;
    }
}
