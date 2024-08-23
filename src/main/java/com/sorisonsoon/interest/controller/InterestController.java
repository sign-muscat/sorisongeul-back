package com.sorisonsoon.interest.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sorisonsoon.interest.service.InterestService;
import com.sorisonsoon.user.controller.UserController;
import com.sorisonsoon.user.domain.type.CustomUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/page")
public class InterestController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final ResourceLoader resourceLoader;
    private final InterestService interestService;

    @PostMapping("/wordcloud")
    public ResponseEntity<String> receiveWordCloudUrl(@AuthenticationPrincipal CustomUser user, @RequestParam("wordCloudUrl") String wordCloudUrl) throws IOException {
        logger.info("수신된 이미지 URL: {}", wordCloudUrl);

        Long userId = user.getUserId();
        interestService.updateWordCloudUrl(userId, wordCloudUrl);

        return ResponseEntity.ok(wordCloudUrl);
    }

    @GetMapping("/wordcloud")
    public ResponseEntity<String> getWordCloudUrl(@AuthenticationPrincipal CustomUser user) {
        Long userId = user.getUserId();
        String wordCloudUrl = interestService.getWordCloudUrl(userId);
        if (wordCloudUrl != null) {
            return ResponseEntity.ok(wordCloudUrl);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}