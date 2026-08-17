package com.jecfalo.palermus_api.modules.users.services;

import com.jecfalo.palermus_api.modules.users.models.Profile;
import com.jecfalo.palermus_api.modules.users.models.User;
import com.jecfalo.palermus_api.modules.users.models.UserType;
import com.jecfalo.palermus_api.modules.users.records.profile.ListProfile;
import com.jecfalo.palermus_api.modules.users.records.profile.ReferenceProfile;
import com.jecfalo.palermus_api.modules.users.records.profile.UpdateProfile;
import com.jecfalo.palermus_api.modules.users.records.profile.UpdateRole;
import com.jecfalo.palermus_api.modules.users.repositories.ProfileRepository;
import com.jecfalo.palermus_api.modules.users.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService implements IProfileService{
    @Autowired
    private ProfileRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<ListProfile> loadAllActive(Pageable page) {
        Page<Profile> profiles = repository.findByProfileActiveTrue(page);
        return profiles.map(ListProfile::new);
    }

    @Override
    @Transactional
    public ReferenceProfile getProfileById(Long profileId) {
        Profile profile = repository.findById(profileId)
                .orElseThrow(()-> new RuntimeException("No existe un perfil con ese documento"));
        return new ReferenceProfile(profile);
    }

    @Override
    public ReferenceProfile getProfileDocument(String document) {
        Profile profile = repository.findByDocument(document)
                .orElseThrow(()-> new RuntimeException("No existe un perfil con ese documento"));
        return new ReferenceProfile(profile);
    }

    @Override
    public ReferenceProfile getProfileByUsername(String username) {
        Profile profile = repository.findByUserUsername(username)
                .orElseThrow(()-> new RuntimeException("No existe un perfil con este usuario"));
        return new ReferenceProfile(profile);
    }

    @Override
    public ReferenceProfile updateProfileEmail(Long id, UpdateProfile emailUpdate) {
        Profile profile = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("No existe un perfil asociado con el identificador introducido"));
        profile.setEmail(emailUpdate.email());
        Profile emailUpdated = repository.save(profile);
        return new ReferenceProfile(emailUpdated);
    }

    @Override
    public ReferenceProfile updateProfileUsername(Long id, UpdateProfile usernameUpdate) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No existe un perfil con el identificador asociado"));
        user.setUsername(usernameUpdate.username());
        User usernameUpdated = userRepository.save(user);
        return new ReferenceProfile(usernameUpdated.getProfile());
    }

    @Override
    public String updateProfilePassword(Long id, UpdateProfile passwordUpdate) {
      User user = userRepository.findById(id)
              .orElseThrow(()-> new EntityNotFoundException("No existe un usuario con e3l identificador asociado"));
      String hashedPassword = passwordEncoder.encode(passwordUpdate.password());
      user.setPassword(hashedPassword);
      userRepository.save(user);
      return "Los datos han sido actualizados";
    }

    @Override
    public ReferenceProfile updateProfileRole(Long id, UpdateRole updateRole){
        Profile profile = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No existe un usuario con el identificador asociado"));
        profile.setUserType(updateRole.type());
        repository.save(profile);
        return new ReferenceProfile(profile);
    }

    @Override
    @Transactional
    public Boolean logicalDelete(Long id) {
        Profile profile = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No existe un usuario con ese identificador asociado"));
        if(!profile.isProfileActive()){
            return false;
        }
        profile.setProfileActive(false);
        repository.save(profile);
        return true;
    }
}
