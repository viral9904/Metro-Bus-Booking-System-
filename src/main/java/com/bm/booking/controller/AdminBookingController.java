package com.bm.booking.controller;

import com.bm.booking.entity.Booking;
import com.bm.booking.entity.BusSeat;
import com.bm.booking.entity.Payment;
import com.bm.booking.repository.BookingRepository;
import com.bm.booking.repository.BusSeatRepository;
import com.bm.booking.repository.PaymentRepository;
import com.bm.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.bm.booking.entity.BookingStatus;

import java.util.*;

@Controller
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingRepository bookingRepository;
    private final BusSeatRepository busSeatRepository;
    private final PaymentRepository paymentRepository;


    @GetMapping
    public String listBookings(Model model) {

        List<Booking> bookings = bookingRepository.findAll();

        Map<Long, List<BusSeat>> seatMap = new HashMap<>();
        Map<Long, Payment> paymentMap = new HashMap<>();

        for (Booking booking : bookings) {
            seatMap.put(
                    booking.getId(),
                    busSeatRepository.findByBooking(booking)
            );

            paymentMap.put(
                    booking.getId(),
                    paymentRepository.findAll()
                            .stream()
                            .filter(p -> p.getBooking().getId().equals(booking.getId()))
                            .findFirst()
                            .orElse(null)
            );
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("seatMap", seatMap);
        model.addAttribute("paymentMap", paymentMap);

        return "admin-bookings";
    }

}