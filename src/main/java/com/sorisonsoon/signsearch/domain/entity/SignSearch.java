package com.sorisonsoon.signsearch.domain.entity;

import com.sorisonsoon.signsearch.domain.type.SignSearchCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "sign_search")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long searchId;

    private int searcherId;
    private String keyword;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(value = EnumType.STRING)
    private SignSearchCategory category;
}
