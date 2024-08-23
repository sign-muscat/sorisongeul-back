package com.sorisonsoon.interest.service;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sorisonsoon.interest.domain.entity.Interest;
import com.sorisonsoon.interest.domain.repository.InterestRepository;
import com.sorisonsoon.user.domain.entity.User;
import com.sorisonsoon.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final UserRepository userRepository;

    @Transactional
    public void updateInterests(Long userId, String keyword) {
        // 유저 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다."));

        // 기존 관심사 삭제
        interestRepository.deleteByUser(user);

        // 새 관심사 추가
        String[] keywords = keyword.split(",");
        for (String kw : keywords) {
            Interest interest = new Interest();
            interest.setKeyword(kw);
            interest.setUser(user);
            interestRepository.save(interest);
        }

        userRepository.save(user);
    }
    private static final Logger logger = LoggerFactory.getLogger(InterestService.class);

    public void updateWordCloudUrl(Long userId, String wordCloudUrl) throws IOException {
        logger.info("Step 1: updateWordCloudUrl 호출됨: userId={}, wordCloudUrl={}", userId, wordCloudUrl);

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("Step 2: 사용자 찾음: user={}", user);

            Optional<Interest> interestOptional = interestRepository.findByUser(user);
            if (interestOptional.isPresent()) {
                Interest interest = interestOptional.get();
                logger.info("Step 3: 관심사 찾음: interest={}", interest);

                interest.setWordCloudUrl(wordCloudUrl);
                interestRepository.save(interest);
                logger.info("Step 4: 관심사 업데이트됨: interestId={}, WordCloudUrl={}", interest.getInterestId(), wordCloudUrl);
            } else {
                logger.info("Step 5: 기존 관심사 없음, 새 관심사 생성");

                Interest newInterest = Interest.builder()
                        .user(user)
                        .wordCloudUrl(wordCloudUrl)
                        .build();
                interestRepository.save(newInterest);
                logger.info("Step 6: 새 관심사 저장됨: newInterest={}", newInterest);
            }
        } else {
            logger.warn("Step 7: 사용자 없음: userId={}", userId);
        }
    }

    public String getWordCloudUrl(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다."));

        Optional<Interest> interestOptional = interestRepository.findByUser(user);
        return interestOptional.map(Interest::getWordCloudUrl).orElse(null);
    }
}
