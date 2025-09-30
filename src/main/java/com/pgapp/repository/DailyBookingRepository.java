package com.pgapp.repository;

import com.pgapp.entity.ApplicationStatus;
import com.pgapp.entity.DailyBookings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DailyBookingRepository extends JpaRepository<DailyBookings, Long> {
    List<DailyBookings> findByStatus(ApplicationStatus status);


    List<DailyBookings> findByRoomIdAndStatusIn(Long roomId, List<ApplicationStatus> statuses);
}
