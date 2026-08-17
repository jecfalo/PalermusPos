package com.jecfalo.palermus_api.modules.users.records.user;

public record RegisterUser(
        String username,
        String password,
        String document,
        String names,
        String surnames,
        String email
) {
}
