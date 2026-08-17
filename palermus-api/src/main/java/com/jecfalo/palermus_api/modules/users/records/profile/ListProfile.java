package com.jecfalo.palermus_api.modules.users.records.profile;

import com.jecfalo.palermus_api.modules.users.models.Profile;
import com.jecfalo.palermus_api.modules.users.models.UserType;
import com.jecfalo.palermus_api.core.security.DataMasker;

public record ListProfile(
        Long id,
        String document,
        String names,
        String surnames,
        String email,
        UserType type,
        Boolean active
) {
    public ListProfile(Profile profile){
        this(
                profile.getId(),
                DataMasker.maskDocument(profile.getDocument()),
                profile.getNames(),
                profile.getSurnames(),
                DataMasker.maskEmail(profile.getEmail()),
                profile.getUserType(),
                profile.isProfileActive()
        );
    }
}
