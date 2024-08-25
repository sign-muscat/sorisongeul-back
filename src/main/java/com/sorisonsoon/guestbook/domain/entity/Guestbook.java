package com.sorisonsoon.guestbook.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.sorisonsoon.guestbook.domain.type.GuestbookStatus;
import com.sorisonsoon.guestbook.dto.GuestBookFormDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "guestbook")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guestbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestbook_id")
    private Long guestbookId;

    private int senderId;
    private int receiverId;
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private GuestbookStatus status = GuestbookStatus.ACTIVATE;

    public static Guestbook createGuestbook(GuestBookFormDto guestBookFormDto) {
        Guestbook guestbook = new Guestbook();
        
        guestbook.setSenderId(guestBookFormDto.getSenderId());
        guestbook.setReceiverId(guestBookFormDto.getReceiverId());
        guestbook.setContent(guestBookFormDto.getContent());
        guestbook.setCreatedAt(guestBookFormDto.getCreatedAt());
        if (guestBookFormDto.getStatus() != null) {
            guestbook.setStatus(guestBookFormDto.getStatus()); 
        }

        return guestbook;
    }
}
