package com.sorisonsoon.ranking.controller;

import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.common.dto.response.ApiResponse;
import com.sorisonsoon.ranking.dto.RankingDTO;
import com.sorisonsoon.ranking.dto.response.RankResponse;
import com.sorisonsoon.ranking.service.RankingService;
import com.sorisonsoon.user.domain.type.CustomUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {

    private static final Logger logger = LoggerFactory.getLogger(RankingController.class);
    private final RankingService rankingService;

//    @PostMapping
//    public ResponseEntity<Void> saveScore(@RequestParam Long userId,
//                                          @RequestParam GameCategory category,
//                                          @RequestParam double score) {
//        logger.info("Saving score for userId: {}, category: {}, score: {}", userId, category, score);
//        rankingService.save(userId, category, score);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> saveScore(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestParam GameCategory category,
            @RequestParam double score
    ) {
        Long userId;
        if(customUser != null) {
            userId = customUser.getUserId();
            rankingService.save(userId, category, score);
        } else {
            return ResponseEntity.ok(ApiResponse.fail(""));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<RankingDTO>> getTop10(@PathVariable String category) {
        logger.info("getTop10 called with category: {}", category);
        try {
            GameCategory gameCategory = GameCategory.valueOf(category.toUpperCase());
            List<RankingDTO> rankings = rankingService.getTop10ByCategory(gameCategory);
            return ResponseEntity.ok(rankings);
        } catch (Exception e) {
            logger.error("Error in getTop10: ", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/today")
    public Map<GameCategory, List<RankResponse>> getTodayRanking(@RequestParam int limit){
        return rankingService.getTodayRanks(limit);
    }

    @GetMapping("/myRank")
    public ResponseEntity<List<RankResponse>> getMyRanking(@AuthenticationPrincipal CustomUser customUser) {
        Long userId;
        List<RankResponse> myRanking;
        if(customUser != null) {
            userId = customUser.getUserId();
            myRanking = rankingService.getMyRanks(userId);
        } else {
            return null;
        }
        return ResponseEntity.ok(myRanking);
    }

}   