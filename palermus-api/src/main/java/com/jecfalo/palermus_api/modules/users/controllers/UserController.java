package com.jecfalo.palermus_api.modules.users.controllers;

import com.jecfalo.palermus_api.modules.users.records.user.ReferenceUser;
import com.jecfalo.palermus_api.modules.users.records.user.RegisterUser;
import com.jecfalo.palermus_api.modules.users.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private IUserService service;
    @PostMapping
    public ResponseEntity<ReferenceUser> userCreated(@RequestBody RegisterUser registerUser, UriComponentsBuilder uriBuilder){
        ReferenceUser user = service.registerUser(registerUser);
        URI uri = uriBuilder.path("/api/user/{id}").buildAndExpand(user.id()).toUri();
        return ResponseEntity.created(uri).body(user);
    }
}
