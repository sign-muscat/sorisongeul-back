package com.sorisonsoon.user.domain.repository;

import com.sorisonsoon.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
