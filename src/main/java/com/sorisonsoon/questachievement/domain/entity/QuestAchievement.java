package com.sorisonsoon.questachievement.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "quest_achievement")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievement_id")
    private Long achievementId;

    private int participantId;
    private int questId;

    @CreatedDate
    private LocalDateTime createdAt;
}
