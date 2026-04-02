package com.bm.booking.controller;

import com.bm.booking.entity.Booking;
import com.bm.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookingService bookingService;

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/cancellations")
    public String viewCancellations(Model model) {

        List<Booking> cancelledBookings = bookingService.findCancelledBookings();

        model.addAttribute("cancelledBookings", cancelledBookings);

        return "admin-cancellations";
    }
}