package com.jecfalo.palermus_api.modules.users.records.profile;

import com.jecfalo.palermus_api.modules.users.models.Profile;
import com.jecfalo.palermus_api.modules.users.models.UserType;
import com.jecfalo.palermus_api.core.security.DataMasker;

public record ReferenceProfile(
        Long id,
        String document,
        String username,
        String names,
        String surnames,
        String email,
        UserType type,
        Boolean active
) {
    public ReferenceProfile(Profile profile){
        this(
                profile.getId(),
                DataMasker.maskDocument(profile.getDocument()),
                profile.getUser().getUsername(),
                profile.getNames(),
                profile.getSurnames(),
                DataMasker.maskEmail(profile.getEmail()),
                profile.getUserType(),
                profile.isProfileActive()
        );
    }
}
