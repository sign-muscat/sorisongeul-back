package com.sorisonsoon.guestbook.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sorisonsoon.guestbook.domain.entity.Guestbook;
import com.sorisonsoon.guestbook.service.GuestbookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/page/guestBook")
@CrossOrigin(origins = "http://localhost:3000")
public class GuestbookController {

    private final GuestbookService guestbookService;

    @GetMapping("/list")
    public List<Guestbook> getGuestbookList(@RequestParam(defaultValue = "3") int limit) {
        return guestbookService.getLatestGuestbooks(limit);
    }
}