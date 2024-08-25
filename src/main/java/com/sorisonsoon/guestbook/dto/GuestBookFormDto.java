package com.sorisonsoon.guestbook.dto;

import java.time.LocalDateTime;

import com.sorisonsoon.guestbook.domain.type.GuestbookStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class GuestBookFormDto {
    private int senderId;
    private int receiverId;
    private String content;
    private LocalDateTime createdAt;
    private GuestbookStatus status;
    
}
