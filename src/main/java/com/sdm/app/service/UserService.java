package com.sdm.app.service;

import com.sdm.app.entity.User;
import com.sdm.app.model.req.create.CreateUserRequest;
import com.sdm.app.model.res.SimpleUserResponse;
import com.sdm.app.model.res.UserResponse;

public interface UserService {

  SimpleUserResponse create(User admin, CreateUserRequest request);
}
