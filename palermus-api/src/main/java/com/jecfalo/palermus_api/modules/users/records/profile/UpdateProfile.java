package com.jecfalo.palermus_api.modules.users.records.profile;

import com.jecfalo.palermus_api.modules.users.models.UserType;

public record UpdateProfile(
        String username,
        String password,
        String email
) {}
