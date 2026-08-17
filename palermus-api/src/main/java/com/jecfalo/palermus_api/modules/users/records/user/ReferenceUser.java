package com.jecfalo.palermus_api.modules.users.records.user;

import com.jecfalo.palermus_api.modules.users.models.User;

public record ReferenceUser(
        Long id,
        String username,
        String names,
        String surnames,
        String email,
        Boolean active
) {
    public ReferenceUser(User user){
        this(
                user.getUserId(),
                user.getUsername(),
                user.getProfile().getNames(),
                user.getProfile().getSurnames(),
                user.getProfile().getEmail(),
                user.isUserActive()
        );
    }
}
