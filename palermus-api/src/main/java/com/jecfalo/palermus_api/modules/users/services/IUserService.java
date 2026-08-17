package com.jecfalo.palermus_api.modules.users.services;

import com.jecfalo.palermus_api.modules.users.records.user.ReferenceUser;
import com.jecfalo.palermus_api.modules.users.records.user.RegisterUser;

public interface IUserService {
    ReferenceUser registerUser(RegisterUser register);
}
