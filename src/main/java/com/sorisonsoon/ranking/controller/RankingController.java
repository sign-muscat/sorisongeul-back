package com.sorisonsoon.ranking.controller;

import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.ranking.dto.RankingDTO;
import com.sorisonsoon.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {

    private static final Logger logger = LoggerFactory.getLogger(RankingController.class);
    private final RankingService rankingService;

    @PostMapping
    public ResponseEntity<Void> saveScore(@RequestParam Long userId,
                                          @RequestParam GameCategory category,
                                          @RequestParam double score) {
        logger.info("Saving score for userId: {}, category: {}, score: {}", userId, category, score);
        rankingService.save(userId, category, score);
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
}   