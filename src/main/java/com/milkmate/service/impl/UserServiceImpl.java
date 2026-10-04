package com.milkmate.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.milkmate.entity.Role;
import com.milkmate.entity.User;
import com.milkmate.repository.UserRepository;
import com.milkmate.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(User user) {

        if (user.getRole() == null) {
            user.setRole(Role.FARMER);
        }

        if (user.getActive() == null) {
            user.setActive(true);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User findByMobile(String mobile) {
        return userRepository.findByMobile(mobile).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User authenticateUser(String mobile, String password) {

        User user = userRepository.findByMobile(mobile).orElse(null);

        if (user == null) {
            return null;
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
            return null;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }
}