package com.sorisonsoon.setting.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "setting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long settingId;

    private int setterId;
    private boolean isRankOpen;
    private boolean isGuestbookOpen;
    private boolean isMedalOpen;
}
