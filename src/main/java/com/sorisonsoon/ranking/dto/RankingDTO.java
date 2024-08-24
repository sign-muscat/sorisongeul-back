package com.sorisonsoon.ranking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingDTO {
    private Long userId;
    private int score;
}