package com.sorisonsoon.teamrecord.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_record_id")
    private Long teamRecordId;

    private int roomId;
    private int whisperId;
    private int questioner;
    private int respondent;
    private boolean isCorrect;

    @CreatedDate
    private LocalDateTime createdAt;

    private String playVideo;
}
