package com.jecfalo.palermus_api.modules.users.records.profile;

import com.jecfalo.palermus_api.modules.users.models.Profile;
import com.jecfalo.palermus_api.modules.users.models.UserType;

public record ReferenceProfile(
        Long id,
        String username,
        String names,
        String usernames,
        String email,
        UserType type,
        Boolean active
) {
    public ReferenceProfile(Profile profile){
        this(
                profile.getId(),
                profile.getUser().getUsername(),
                profile.getNames(),
                profile.getSurnames(),
                profile.getEmail(),
                profile.getUserType(),
                profile.isProfileActive()
        );
    }
}
