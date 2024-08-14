package com.sorisonsoon.friend.domain.repository;

import com.sorisonsoon.friend.domain.entity.Friend;
import com.sorisonsoon.friend.domain.type.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {

    Optional<Friend> findByFriendIdAndToUserAndStatus(Long friendId, Long userId, FriendStatus friendStatus);
}
