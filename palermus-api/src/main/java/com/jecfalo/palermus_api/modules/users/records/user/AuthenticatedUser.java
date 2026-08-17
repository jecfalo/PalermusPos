package com.jecfalo.palermus_api.modules.users.records.user;

public record AuthenticatedUser(
        String username,
        String password
) {
}
