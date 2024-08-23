package com.sorisonsoon.interest.dto;

import lombok.*;

@Getter @Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestFormDto {
    private Long interestId;

    private Long userId;

    private String keyword;

    private String wordCloudUrl;
}
