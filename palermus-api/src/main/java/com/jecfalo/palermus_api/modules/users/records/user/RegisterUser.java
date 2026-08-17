package com.jecfalo.palermus_api.modules.users.records.user;

import com.jecfalo.palermus_api.modules.users.models.UserType;

public record RegisterUser(
        String username,
        String password,
        String document,
        String names,
        String surnames,
        String email,
        UserType userType
) {
}
