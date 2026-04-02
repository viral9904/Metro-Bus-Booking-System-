package com.bm.booking.controller;

import com.bm.booking.entity.Booking;
import com.bm.booking.entity.BusSeat;
import com.bm.booking.entity.User;
import com.bm.booking.entity.Schedule;
import com.bm.booking.entity.SeatClass;
import com.bm.booking.repository.BusSeatRepository;
import com.bm.booking.service.BookingService;
import com.bm.booking.service.ScheduleService;
import com.bm.booking.service.SeatClassService;
import com.bm.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserBookingController {

    private final ScheduleService scheduleService;
    private final BookingService bookingService;
    private final UserService userService;
    private final SeatClassService seatClassService;
    private final BusSeatRepository busSeatRepository;


    // =====================================
    // Show Seat Selection Page
    // =====================================
    @GetMapping("/bus/seats/{scheduleId}")
    public String showSeatSelection(@PathVariable Long scheduleId,
                                    @RequestParam int ticketCount,
                                    Model model) {

        List<BusSeat> seats = scheduleService.getSeatsBySchedule(scheduleId);

        System.out.println("Seats fetched: " + seats.size());
        System.out.println("Seats size: " + seats.size());

        model.addAttribute("seats", seats);
        model.addAttribute("scheduleId", scheduleId);
        model.addAttribute("ticketCount", ticketCount);

        return "bus-seat-selection";
    }
    // =====================================
// Confirm Selected Seats (Lock Seats)
// =====================================
    @PostMapping("/bus/confirm-seats")
    public String confirmSeats(@RequestParam Long scheduleId,
                               @RequestParam int ticketCount,
                               @RequestParam String selectedSeats,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {

        if (selectedSeats == null || selectedSeats.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select seats.");
            return "redirect:/user/bus/seats/" + scheduleId + "?ticketCount=" + ticketCount;
        }

        String[] seatNumbers = selectedSeats.split(",");

        if (seatNumbers.length != ticketCount) {
            redirectAttributes.addFlashAttribute("error", "Seat count mismatch.");
            return "redirect:/user/bus/seats/" + scheduleId + "?ticketCount=" + ticketCount;
        }

        User user = userService.findByEmail(principal.getName());
        Schedule schedule = scheduleService.findById(scheduleId);


        SeatClass seatClass = schedule.getVehicle().getSeatClass();

        Booking booking = bookingService.createBookingWithSeats(
                user,
                schedule,
                seatClass,
                seatNumbers
        );

        return "redirect:/user/book/" + booking.getId();
    }
}