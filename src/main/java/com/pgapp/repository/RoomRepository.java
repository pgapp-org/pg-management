package com.pgapp.repository;

import com.pgapp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository  extends JpaRepository<Room, Long> {
    List<Room> findByFloorId(Long floorId);
}