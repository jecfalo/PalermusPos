package com.jecfalo.palermus_api.modules.users.records.profile;

import com.jecfalo.palermus_api.modules.users.models.Profile;
import com.jecfalo.palermus_api.modules.users.models.UserType;

public record ListProfile(
        Long id,
        String names,
        String surnames,
        String email,
        UserType type,
        Boolean active
) {
    public ListProfile(Profile profile){
        this(
                profile.getId(),
                profile.getNames(),
                profile.getSurnames(),
                profile.getEmail(),
                profile.getUserType(),
                profile.isProfileActive()
        );
    }
}
