package com.sorisonsoon.friend.domain.repository;

import com.sorisonsoon.friend.domain.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

}
