package com.sorisonsoon.user.domain.repository;

import com.sorisonsoon.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);

    Optional<User> findByRefreshToken(String refreshToken);

}
