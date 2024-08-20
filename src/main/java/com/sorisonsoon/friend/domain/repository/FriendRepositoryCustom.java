package com.sorisonsoon.friend.domain.repository;

import com.sorisonsoon.friend.domain.type.ApplyType;
import com.sorisonsoon.friend.dto.response.FriendApplyResponse;
import com.sorisonsoon.friend.dto.response.FriendResponse;
import com.sorisonsoon.friend.dto.response.RecommendFriendResponse;

import java.util.List;

public interface FriendRepositoryCustom {
    List<FriendResponse> getFriends(Long userId);

    List<FriendApplyResponse> getFriendAppliesFrom(Long userId);

    List<FriendApplyResponse> getFriendAppliesTo(Long userId);

    List<RecommendFriendResponse> getRecommendedFriends(List<Long> recommends);
}
