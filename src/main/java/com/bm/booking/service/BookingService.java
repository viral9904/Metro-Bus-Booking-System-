package com.bm.booking.service;

import com.bm.booking.entity.*;
import com.bm.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public Booking createBooking(User user,
                                 Schedule schedule,
                                 SeatClass seatClass) {

        double fare = seatClass.getBaseFare();

        Booking booking = Booking.builder()
                .user(user)
                .schedule(schedule)
                .seatClass(seatClass)
                .totalFare(fare)
                .status(BookingStatus.PENDING)
                .build();

        return bookingRepository.save(booking);
    }

    public List<Booking> findByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow();
    }
}