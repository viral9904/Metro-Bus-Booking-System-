package com.bm.booking.service;

import com.bm.booking.entity.*;
import com.bm.booking.repository.BookingRepository;
import com.bm.booking.repository.BusSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bm.booking.entity.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BusSeatRepository busSeatRepository;


    @Transactional
    public Booking createBookingWithSeats(User user,
                                          Schedule schedule,
                                          SeatClass seatClass,
                                          String[] seatNumbers) {

        double distance = schedule.getRoute().getDistanceKm();
        double base = seatClass.getBaseFare();
        double rate = seatClass.getRatePerKm();

        double farePerSeat = base + (distance * rate);
        double totalFare = farePerSeat * seatNumbers.length;

        Booking booking = Booking.builder()
                .user(user)
                .schedule(schedule)
                .seatClass(seatClass)
                .totalFare(totalFare)
                .status(BookingStatus.PENDING)
                .build();

        booking = bookingRepository.save(booking);

        for (String seatNumber : seatNumbers) {

            BusSeat seat = busSeatRepository
                    .findByScheduleAndSeatNumber(schedule, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new RuntimeException("Seat already booked or locked");
            }

            seat.setStatus(SeatStatus.LOCKED);
            seat.setLockedAt(LocalDateTime.now());
            seat.setBooking(booking);
        }

        return booking;
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public List<Booking> findByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public List<Booking> findCancelledBookings() {
        return bookingRepository.findByStatus(BookingStatus.CANCELLED);
    }
}