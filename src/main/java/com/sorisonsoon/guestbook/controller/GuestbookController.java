package com.sorisonsoon.guestbook.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sorisonsoon.guestbook.domain.entity.Guestbook;
import com.sorisonsoon.guestbook.dto.GuestBookFormDto;
import com.sorisonsoon.guestbook.service.GuestbookService;
import com.sorisonsoon.user.controller.UserController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/page/guestBook")
@CrossOrigin(origins = "http://localhost:3000")
public class GuestbookController {

    private final GuestbookService guestbookService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/new")
    public ResponseEntity<String> createGuestbook(@RequestBody @Valid GuestBookFormDto guestBookFormDto, BindingResult bindingResult) {
                logger.info("방명록 생성 요청을 받았습니다.");

        try {
            logger.info("받은 방명록 데이터: {}" , guestBookFormDto);

            Guestbook guestbook = Guestbook.createGuestbook(guestBookFormDto);
            guestbookService.saveGuestbook(guestbook);
            guestbook.setCreatedAt(LocalDateTime.now());
            guestbookService.saveGuestbook(guestbook);

            return ResponseEntity.status(HttpStatus.CREATED).body("Guestbook entry created successfully.");
        } catch (IllegalArgumentException e) {
            // Handle invalid status or other conversion issues
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status value provided.");
        } catch (Exception e) {
            // Log the exception and return an error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the guestbook entry.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Guestbook>> getGuestbookList(@RequestParam(defaultValue = "3") int limit) {
        List<Guestbook> guestbooks = guestbookService.getLatestGuestbooks(limit);
        return ResponseEntity.ok(guestbooks);
    }
}
