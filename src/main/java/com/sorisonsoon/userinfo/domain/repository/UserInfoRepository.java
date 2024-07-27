package com.sorisonsoon.userinfo.domain.repository;

import com.sorisonsoon.userinfo.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

}
