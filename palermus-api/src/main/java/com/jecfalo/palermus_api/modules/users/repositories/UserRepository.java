package com.jecfalo.palermus_api.modules.users.repositories;

import com.jecfalo.palermus_api.modules.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfile extends JpaRepository<User, Long> {
}
