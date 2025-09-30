package com.pgapp.repository;


import com.pgapp.entity.DailyBookingGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DailyBookingGuestRepository extends JpaRepository<DailyBookingGuest, Long> {
    @Query("""
        SELECT g FROM DailyBookingGuest g
        WHERE g.bed.id = :bedId
          AND g.booking.status = 'APPROVED'
          AND g.booking.checkInDate <= :checkOut
          AND g.booking.checkOutDate >= :checkIn
    """)
    List<DailyBookingGuest> findConflictingGuestsForBed(
            @Param("bedId") Long bedId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    @Query("SELECT g FROM DailyBookingGuest g " +
            "WHERE g.bed.id = :bedId " +
            "AND g.booking.status = 'APPROVED' " +
            "AND :date BETWEEN g.booking.checkInDate AND g.booking.checkOutDate")
    List<DailyBookingGuest> findShortTermGuestsForBed(@Param("bedId") Long bedId, @Param("date") LocalDate date);

    List<DailyBookingGuest> findByBedId(Long bedId);

    @Query("SELECT g FROM DailyBookingGuest g " +
            "WHERE g.room.id = :roomId AND g.booking.status = 'APPROVED'")
    List<DailyBookingGuest> findByRoomId(@Param("roomId") Long roomId);
}
