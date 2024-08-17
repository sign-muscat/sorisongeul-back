package com.sorisonsoon.user.domain.repository;

import com.sorisonsoon.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);

    Optional<User> findByRefreshToken(String refreshToken);

    @Query("SELECT r.refreshToken FROM User r WHERE r.id = :id")
    String findRefreshTokenById(@Param("id") String id);

}
