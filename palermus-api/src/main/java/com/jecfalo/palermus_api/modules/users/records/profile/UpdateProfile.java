package com.jecfalo.palermus_api.modules.users.records.profile;

public record UpdateProfile(
        String username,
        String currentPassword,
        String password,
        String email
) {}
