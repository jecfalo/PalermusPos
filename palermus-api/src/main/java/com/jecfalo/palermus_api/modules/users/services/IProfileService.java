package com.jecfalo.palermus_api.modules.users.services;

import com.jecfalo.palermus_api.modules.users.records.profile.ListProfile;
import com.jecfalo.palermus_api.modules.users.records.profile.ReferenceProfile;
import com.jecfalo.palermus_api.modules.users.records.profile.UpdateProfile;
import com.jecfalo.palermus_api.modules.users.records.profile.UpdateRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;

public interface IProfileService {
    Page<ListProfile> loadAllActive(Pageable page);
    ReferenceProfile getProfileById(Long profileId);
    ReferenceProfile getProfileDocument(String document);
    ReferenceProfile getProfileByUsername(String username);
    ReferenceProfile updateProfileEmail(Long id, UpdateProfile emailUpdate);
    ReferenceProfile updateProfileUsername(Long id, UpdateProfile usernameUpdate);
    String updateProfilePassword(Long id, UpdateProfile passwordUpdate);
    ReferenceProfile updateProfileRole(Long id, UpdateRole updateRole);
    Boolean logicalDelete(Long id);
}
