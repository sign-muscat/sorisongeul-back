package com.sorisonsoon.friend.controller;

import com.sorisonsoon.friend.domain.type.ApplyType;
import com.sorisonsoon.friend.domain.type.FriendStatus;
import com.sorisonsoon.friend.dto.response.FriendApplyResponse;
import com.sorisonsoon.friend.dto.response.FriendResponse;
import com.sorisonsoon.friend.dto.response.RecommendFriendResponse;
import com.sorisonsoon.friend.service.FriendService;
import com.sorisonsoon.user.domain.type.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/friends")
    public ResponseEntity<List<FriendResponse>> getFriends(@AuthenticationPrincipal CustomUser user) {
        List<FriendResponse> result = friendService.getFriends(user.getUserId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/friends-apply")
    public ResponseEntity<List<FriendApplyResponse>> getFriendApplies(@RequestParam ApplyType applyType,
                                                                      @AuthenticationPrincipal CustomUser user
    ) {
        List<FriendApplyResponse> result = friendService.getFriendApplies(user.getUserId(), applyType);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/friends/{toUser}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable final Long toUser,
                                                  @AuthenticationPrincipal CustomUser user
    ) {
        friendService.save(user.getUserId(), toUser);

        return ResponseEntity.created(URI.create("api/v1/friends-apply")).build();
    }

    @PutMapping("/friends/{friendId}")
    public ResponseEntity<Void> handleFriendRequest(@PathVariable final Long friendId,
                                                    @RequestParam final FriendStatus status,
                                                    @AuthenticationPrincipal CustomUser user
    ) {
        friendService.modify(user.getUserId(), friendId, status);

        return ResponseEntity.created(URI.create("api/v1/friends-apply")).build();
    }

    @DeleteMapping("/friends-apply/{friendId}")
    public ResponseEntity<Void> cancelFriendApply(@PathVariable final Long friendId,
                                                  @AuthenticationPrincipal CustomUser user
    ) {
        friendService.cancelFriendApply(user.getUserId(), friendId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/friends/{friendId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable final Long friendId,
                                             @AuthenticationPrincipal CustomUser user
    ) {
        friendService.deleteFriend(user.getUserId(), friendId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/friends/recommend")
    public ResponseEntity<List<RecommendFriendResponse>> getRecommendations(@AuthenticationPrincipal CustomUser user) {

        List<RecommendFriendResponse> result = friendService.getRecommendedFriends(user.getUserId());

        return ResponseEntity.ok(result);
    }
}
