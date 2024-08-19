package com.sorisonsoon.interest.service;

import org.springframework.stereotype.Service;

import com.sorisonsoon.interest.domain.entity.Interest;
import com.sorisonsoon.interest.domain.repository.InterestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public void updateInterests(Long userId, String keyword) {
        // 기존 관심사 삭제
        interestRepository.deleteByUserId(userId);
    
        // 키워드가 단일 단어인지 확인
        if (keyword.trim().contains(" ")) {
            throw new IllegalArgumentException("키워드는 한 단어만 포함할 수 있습니다.");
        }
    
        // 새 관심사 추가
        Interest interest = new Interest();
        interest.setUserId(userId);
        interest.setKeyword(keyword.trim());
        interestRepository.save(interest);
    }
    
}

