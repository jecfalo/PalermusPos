package com.jecfalo.palermus_api.modules.users.services;

import com.jecfalo.palermus_api.modules.users.models.Profile;
import com.jecfalo.palermus_api.modules.users.models.User;
import com.jecfalo.palermus_api.modules.users.models.UserType;
import com.jecfalo.palermus_api.modules.users.records.user.ReferenceUser;
import com.jecfalo.palermus_api.modules.users.records.user.RegisterUser;
import com.jecfalo.palermus_api.modules.users.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements  IUserService{
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder encoder;
    @Override
    @Transactional
    public ReferenceUser registerUser(RegisterUser register) {
        User user = new User();
        String rawUsername = (register.username() == null || register.username().trim().isEmpty())
                             ? register.document()
                             : register.username();
        user.setUsername(rawUsername);
        
        String rawPassword = (register.password() == null || register.password().trim().isEmpty()) 
                             ? register.document() 
                             : register.password();
        
        user.setPassword(encoder.encode(rawPassword));
        user.setUserActive(true);
        
        UserType finalRole = (register.userType() != null) ? register.userType() : UserType.CLIENT;
        
        Profile profile = Profile.builder()
                .document(register.document())
                .names(register.names())
                .surnames(register.surnames())
                .email(register.email())
                .userType(finalRole)
                .profileActive(true)
                .build();
        profile.setUser(user);
        user.setProfile(profile);
        User userRegistered = repository.save(user);
        return new ReferenceUser(userRegistered);
    }
}
