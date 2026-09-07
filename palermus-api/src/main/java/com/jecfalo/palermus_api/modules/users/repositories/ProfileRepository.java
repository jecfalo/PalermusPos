package com.jecfalo.palermus_api.modules.users.repositories;

import com.jecfalo.palermus_api.modules.users.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    //indica que en la primera consulta debe traer todos a traves de un left outer join
    @EntityGraph(attributePaths = {"user"})
    Page<Profile> findByProfileActiveTrue(Pageable page);
    Optional<Profile> findByDocument(String document);
    Optional<Profile> findByUserUsername(String username);
}
