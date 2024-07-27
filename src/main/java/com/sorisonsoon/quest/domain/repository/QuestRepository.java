package com.sorisonsoon.quest.domain.repository;

import com.sorisonsoon.quest.domain.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {

}
