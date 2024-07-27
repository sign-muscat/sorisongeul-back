package com.sorisonsoon.room.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE room SET deleted_at = CURRENT_TIMESTAMP WHERE room_id = ? ")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    private int constructorId;
    private int participantId;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;
}
