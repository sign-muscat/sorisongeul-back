package com.sorisonsoon.friend.controller;

import com.sorisonsoon.friend.domain.type.ApplyType;
import com.sorisonsoon.friend.domain.type.FriendStatus;
import com.sorisonsoon.friend.dto.response.FriendApplyResponse;
import com.sorisonsoon.friend.dto.response.FriendResponse;
import com.sorisonsoon.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/friends")
    public ResponseEntity<List<FriendResponse>> getFriends() {
        // Test 데이터 @AuthenticationPrincipal
        Long userId = 1L;

        List<FriendResponse> result = friendService.getFriends(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/friends-apply")
    public ResponseEntity<List<FriendApplyResponse>> getFriendApplies(@RequestParam ApplyType applyType) {
        // Test 데이터 @AuthenticationPrincipal
        Long userId = 2L;

        List<FriendApplyResponse> result = friendService.getFriendApplies(userId, applyType);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/friends/{toUser}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable final Long toUser) {
        // Test 데이터 @AuthenticationPrincipal
        Long fromUser = 1L;

        friendService.save(fromUser, toUser);

        return ResponseEntity.created(URI.create("api/v1/friends-apply")).build();
    }

    @PutMapping("/friends/{friendId}")
    public ResponseEntity<Void> handleFriendRequest(@PathVariable final Long friendId,
                                                    @RequestParam final FriendStatus status) {
        // Test 데이터 @AuthenticationPrincipal
        Long userId = 1L;

        friendService.modify(userId, friendId, status);

        return ResponseEntity.created(URI.create("api/v1/friends-apply")).build();
    }

}
