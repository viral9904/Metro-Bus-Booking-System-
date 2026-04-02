package com.bm.booking.repository;

import com.bm.booking.entity.Booking;
import com.bm.booking.entity.BookingStatus;
import com.bm.booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(User user);
    boolean existsBySchedule_Id(Long scheduleId);
    List<Booking> findByStatus(BookingStatus status);
}