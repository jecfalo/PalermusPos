package com.jecfalo.palermus_api.modules.users.repositories;

import com.jecfalo.palermus_api.modules.users.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Page<Profile> findByProfileActiveTrue(Pageable page);
    Optional<Profile> findByDocument(String document);
    Optional<Profile> findByUserUsername(String username);
}
