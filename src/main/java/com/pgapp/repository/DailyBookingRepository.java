package com.pgapp.repository;

import com.pgapp.enums.ApplicationStatus;
import com.pgapp.entity.DailyBookings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyBookingRepository extends JpaRepository<DailyBookings, Long> {
    List<DailyBookings> findByStatus(ApplicationStatus status);


    List<DailyBookings> findByRoomIdAndStatusIn(Long roomId, List<ApplicationStatus> statuses);

    List<DailyBookings> findByPgIdAndStatus(Long pgId, ApplicationStatus status);
}
