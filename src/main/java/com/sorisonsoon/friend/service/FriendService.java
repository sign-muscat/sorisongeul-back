package com.sorisonsoon.friend.service;

import com.sorisonsoon.common.exception.BadRequestException;
import com.sorisonsoon.common.exception.type.ExceptionCode;
import com.sorisonsoon.friend.domain.entity.Friend;
import com.sorisonsoon.friend.domain.repository.FriendRepository;
import com.sorisonsoon.friend.domain.type.ApplyType;
import com.sorisonsoon.friend.domain.type.FriendStatus;
import com.sorisonsoon.friend.dto.response.FriendApplyResponse;
import com.sorisonsoon.friend.dto.response.FriendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    @Transactional(readOnly = true)
    public List<FriendResponse> getFriends(Long userId) {
        return friendRepository.getFriends(userId);
    }

    @Transactional(readOnly = true)
    public List<FriendApplyResponse> getFriendApplies(Long userId, ApplyType applyType) {
        if(ApplyType.FROM_ME.equals(applyType))
            return friendRepository.getFriendAppliesFrom(userId);
        else if(ApplyType.TO_ME.equals(applyType))
            return friendRepository.getFriendAppliesTo(userId);
        else
            throw new BadRequestException(ExceptionCode.INVALID_APPLY_TYPE);
    }

    public void save(Long fromUser, Long toUser) {
        final Friend newFriend = Friend.of(fromUser, toUser);

        friendRepository.save(newFriend);
    }

    public void modify(Long userId, Long friendId, FriendStatus status) {
        Friend friend = friendRepository.findByFriendIdAndToUserAndStatus(friendId, userId, FriendStatus.APPLIED)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.ACCESS_DENIED));

        friend.updateStatus(status);
    }
}
