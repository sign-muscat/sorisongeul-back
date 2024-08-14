package com.sorisonsoon.friend.controller;

import com.sorisonsoon.friend.domain.type.ApplyType;
import com.sorisonsoon.friend.dto.response.FriendApplyResponse;
import com.sorisonsoon.friend.dto.response.FriendResponse;
import com.sorisonsoon.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/friends")
    public ResponseEntity<List<FriendResponse>> getFriends() {
        // Test 데이터
        Long userId = 1L;

        List<FriendResponse> result = friendService.getFriends(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/friends-apply")
    public ResponseEntity<List<FriendApplyResponse>> getFriendApplies(@RequestParam ApplyType applyType) {
        // Test 데이터
        Long userId = 2L;

        List<FriendApplyResponse> result = friendService.getFriendApplies(userId, applyType);

        return ResponseEntity.ok(result);
    }
}
