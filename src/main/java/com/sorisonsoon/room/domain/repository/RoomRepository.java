package com.sorisonsoon.room.domain.repository;

import com.sorisonsoon.room.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
