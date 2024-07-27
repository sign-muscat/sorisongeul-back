package com.sorisonsoon.community.domain.repository;

import com.sorisonsoon.community.domain.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {

}
