package com.sorisonsoon.guestbook.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.sorisonsoon.guestbook.domain.entity.Guestbook;
import com.sorisonsoon.guestbook.domain.repository.GuestbookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuestbookService {

    private final GuestbookRepository guestbookRepository;

    public Guestbook saveGuestbook(Guestbook guestbook) {
        return guestbookRepository.save(guestbook);
    }

    public List<Guestbook> getLatestGuestbooks(int limit) {
        return guestbookRepository.findByOrderByCreatedAtDesc(PageRequest.of(0, limit));
    }
    
}
