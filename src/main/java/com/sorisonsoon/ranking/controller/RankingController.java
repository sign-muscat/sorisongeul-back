package com.sorisonsoon.ranking.controller;

import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.ranking.dto.RankingDTO;
import com.sorisonsoon.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rankings")
public class RankingController {
    private final RankingService rankingService;

    @PostMapping
    public ResponseEntity<Void> saveScore(@RequestParam Long userId,
                                          @RequestParam GameCategory category,
                                          @RequestParam double score) {
        rankingService.save(userId, category, score);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<RankingDTO>> getTop10(@PathVariable GameCategory category) {
        List<RankingDTO> rankings = rankingService.getTop10ByCategory(category);
        return ResponseEntity.ok(rankings);
    }
}