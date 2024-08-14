package com.sorisonsoon.friend.domain.repository;

import com.sorisonsoon.friend.domain.type.ApplyType;
import com.sorisonsoon.friend.dto.response.FriendApplyResponse;
import com.sorisonsoon.friend.dto.response.FriendResponse;

import java.util.List;

public interface FriendRepositoryCustom {
    List<FriendResponse> getFriends(Long userId);

    List<FriendApplyResponse> getFriendAppliesFrom(Long userId);

    List<FriendApplyResponse> getFriendAppliesTo(Long userId);
}
