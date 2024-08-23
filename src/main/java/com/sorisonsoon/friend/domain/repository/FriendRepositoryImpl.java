package com.sorisonsoon.friend.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.friend.domain.type.FriendStatus;
import com.sorisonsoon.friend.dto.response.FriendApplyResponse;
import com.sorisonsoon.friend.dto.response.FriendResponse;
import com.sorisonsoon.friend.dto.response.RecommendFriendResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sorisonsoon.friend.domain.entity.QFriend.friend;
import static com.sorisonsoon.user.domain.entity.QUser.user;
import static com.sorisonsoon.userinfo.domain.entity.QUserInfo.userInfo;

@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<FriendResponse> getFriends(Long userId) {
        BooleanExpression friendCondition = Expressions.booleanTemplate(
                "(({0} = {1} and {2} = {3}) or ({4} = {5} and {6} = {7}))",
                friend.fromUser, userId, friend.toUser, user.userId,
                friend.toUser, userId, friend.fromUser, user.userId
        );

        return queryFactory
                .select(Projections.constructor(FriendResponse.class,
                        friend.friendId,
                        user.userId,
                        user.nickname,
                        user.profileImage,
                        userInfo.level
                ))
                .from(friend)
                .join(user).on(friendCondition.and(friend.status.eq(FriendStatus.ACCEPTED)))
                .leftJoin(userInfo).on(user.userId.eq(userInfo.userId))
                .fetch();
    }

    @Override
    public List<FriendApplyResponse> getFriendAppliesFrom(Long userId) {
        return queryFactory
                .select(Projections.constructor(FriendApplyResponse.class,
                        friend.friendId,
                        user.nickname,
                        user.profileImage,
                        friend.createdAt,
                        friend.status
                ))
                .from(friend)
                .join(user).on(user.userId.eq(friend.toUser))
                .where(friend.fromUser.eq(userId).and(friend.status.eq(FriendStatus.APPLIED)))
                .fetch();
    }

    @Override
    public List<FriendApplyResponse> getFriendAppliesTo(Long userId) {
        return queryFactory
                .select(Projections.constructor(FriendApplyResponse.class,
                        friend.friendId,
                        user.nickname,
                        user.profileImage,
                        friend.createdAt,
                        friend.status
                ))
                .from(friend)
                .join(user).on(user.userId.eq(friend.fromUser))
                .where(friend.toUser.eq(userId).and(friend.status.eq(FriendStatus.APPLIED)))
                .fetch();
    }

    @Override
    public List<RecommendFriendResponse> getRecommendedFriends(List<Long> recommends) {
        return queryFactory
                .select(Projections.constructor(RecommendFriendResponse.class,
                        user.userId,
                        user.nickname,
                        user.profileImage,
                        userInfo.level
                ))
                .from(user)
                .leftJoin(userInfo).on(user.userId.eq(userInfo.userId))
                .where(user.userId.in(recommends))
                .fetch();
    }
}
