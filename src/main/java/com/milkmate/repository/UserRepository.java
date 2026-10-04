package com.milkmate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milkmate.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMobile(String mobile);

    Optional<User> findByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByEmail(String email);
}