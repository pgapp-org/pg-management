package com.pgapp.repository;

import com.pgapp.entity.Room;
import com.pgapp.entity.TenantApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository  extends JpaRepository<Room, Long> {
    List<Room> findByFloorId(Long floorId);
    List<Room> findByPgId(Long pgId);
    Optional<Room> findByFloorIdAndRoomNumber(Long floorId, String roomNumber);
//    Optional<TenantApplication> findByTenantIdAndCheckoutConfirmedFalse(Long tenantId);
}