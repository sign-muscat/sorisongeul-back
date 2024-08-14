package com.sorisonsoon.friend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sorisonsoon.friend.domain.type.FriendStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class FriendApplyResponse {

    private final Long friendId;
    private final String nickname;
    private final String profileImage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime applyDate;
    private final FriendStatus status;

}
