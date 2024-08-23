package com.sorisonsoon.friend.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FriendResponse {

    private final Long friendId;
    private final Long userId;
    private final String nickname;
    private final String profileImage;
    private final Integer level;

}
