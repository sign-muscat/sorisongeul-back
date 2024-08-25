package com.sorisonsoon.friend.service;

import com.sorisonsoon.common.exception.BadRequestException;
import com.sorisonsoon.common.exception.NotFoundException;
import com.sorisonsoon.common.exception.type.ExceptionCode;
import com.sorisonsoon.friend.domain.entity.Friend;
import com.sorisonsoon.friend.domain.repository.FriendRepository;
import com.sorisonsoon.friend.domain.type.ApplyType;
import com.sorisonsoon.friend.domain.type.FriendStatus;
import com.sorisonsoon.friend.dto.response.RecommendFriendResponse;
import com.sorisonsoon.friend.dto.response.FriendApplyResponse;
import com.sorisonsoon.friend.dto.response.FriendResponse;
import com.sorisonsoon.interest.domain.entity.Interest;
import com.sorisonsoon.interest.domain.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.sorisonsoon.common.utils.sentenceSimilarity.MatrixUtils.cosineSimilarity;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final InterestRepository interestRepository;
    private final TransformersEmbeddingModel transformersEmbeddingModel;

    @Transactional(readOnly = true)
    public List<FriendResponse> getFriends(Long userId) {
        return friendRepository.getFriends(userId);
    }

    @Transactional(readOnly = true)
    public List<FriendApplyResponse> getFriendApplies(Long userId, ApplyType applyType) {
        if(ApplyType.FROM_ME.equals(applyType))
            return friendRepository.getFriendAppliesFrom(userId);
        else if(ApplyType.TO_ME.equals(applyType))
            return friendRepository.getFriendAppliesTo(userId);
        else
            throw new BadRequestException(ExceptionCode.INVALID_APPLY_TYPE);
    }

    public void save(Long fromUser, Long toUser) {
        final Friend newFriend = Friend.of(fromUser, toUser);

        friendRepository.save(newFriend);
    }

    public void modify(Long userId, Long friendId, FriendStatus status) {
        Friend friend = friendRepository.findByFriendIdAndToUserAndStatus(friendId, userId, FriendStatus.APPLIED)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.ACCESS_DENIED));

        friend.updateStatus(status);
    }

    public void cancelFriendApply(Long userId, Long friendId) {
        Friend friend = friendRepository.findByFriendIdAndFromUserAndStatus(friendId, userId, FriendStatus.APPLIED)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.ACCESS_DENIED));

        friend.updateStatus(FriendStatus.CANCELED);
    }

    public void deleteFriend(Long userId, Long friendId) {
        Friend friend = friendRepository.findByFriendIdAndStatus(friendId, FriendStatus.ACCEPTED)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_FRIEND));

        if(!friend.getFromUser().equals(userId) && !friend.getToUser().equals(userId)) {
            throw new BadRequestException(ExceptionCode.ACCESS_DENIED);
        }

        friendRepository.delete(friend);
    }

    @Transactional(readOnly = true)
    public List<RecommendFriendResponse> getRecommendedFriends(Long userId) {
        List<Interest> interests = interestRepository.findAll();

        Map<Long, List<List<Double>>> userEmbeddings = new HashMap<>();

        for (Interest interest : interests) {
            List<Double> embedding = transformersEmbeddingModel.embed(interest.getKeyword());
            userEmbeddings.computeIfAbsent(interest.getUser().getUserId(), k -> new ArrayList<>()).add(embedding);
        }

        Map<Long, Double> similarityMap = new HashMap<>();
        List<List<Double>> targetEmbeddings = userEmbeddings.get(userId);

        for (Map.Entry<Long, List<List<Double>>> entry : userEmbeddings.entrySet()) {
            Long otherUserId = entry.getKey();
            if (otherUserId.equals(userId)) continue;

            List<List<Double>> otherEmbeddings = entry.getValue();
            double maxSimilarity = 0.0;

            for (List<Double> targetVec : targetEmbeddings) {
                for (List<Double> otherVec : otherEmbeddings) {
                    double similarity = cosineSimilarity(
                            targetVec, otherVec
                    );
                    maxSimilarity = Math.max(maxSimilarity, similarity);
                }
            }
            similarityMap.put(otherUserId, maxSimilarity);
        }

        List<Long> recommends = similarityMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        System.out.println("similarityMap : " + similarityMap);
        System.out.println("recommends : " + recommends);
        return friendRepository.getRecommendedFriends(recommends);
    }
}
