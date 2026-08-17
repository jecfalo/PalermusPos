package com.jecfalo.palermus_api.modules.users.repositories;

import com.jecfalo.palermus_api.modules.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.profile WHERE u.username = :username AND u.userActive = true")
    UserDetails findByUsername(@Param("username") String username);
}
