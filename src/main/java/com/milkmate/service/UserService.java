package com.milkmate.service;

import com.milkmate.entity.User;

public interface UserService {

    User registerUser(User user);

    User findByMobile(String mobile);

    User findByEmail(String email);

    User authenticateUser(String mobile, String password);
}