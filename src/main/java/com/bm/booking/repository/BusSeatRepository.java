package com.bm.booking.repository;
import com.bm.booking.entity.Booking;
import com.bm.booking.entity.SeatStatus;

import com.bm.booking.entity.BusSeat;
import com.bm.booking.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

public interface BusSeatRepository extends JpaRepository<BusSeat, Long> {

    List<BusSeat> findBySchedule(Schedule schedule);
    Optional<BusSeat> findByScheduleAndSeatNumber(Schedule schedule, String seatNumber);
    List<BusSeat> findByStatusAndLockedAtBefore(
            SeatStatus status,
            LocalDateTime time);
    List<BusSeat> findByBooking(Booking booking);
    long countByBooking(Booking booking);
    boolean existsBySchedule(Schedule schedule);
    List<BusSeat> findByScheduleId(Long scheduleId);

}