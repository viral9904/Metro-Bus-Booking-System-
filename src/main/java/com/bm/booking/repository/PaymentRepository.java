package com.bm.booking.repository;

import com.bm.booking.entity.Booking;
import com.bm.booking.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByBooking(Booking booking);
}