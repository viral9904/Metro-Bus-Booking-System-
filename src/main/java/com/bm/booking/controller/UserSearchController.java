package com.bm.booking.controller;

import com.bm.booking.entity.*;
import com.bm.booking.repository.BusSeatRepository;
import com.bm.booking.repository.PassengerRepository;
import com.bm.booking.repository.UserRepository;
import com.bm.booking.service.BookingService;
import com.bm.booking.service.ScheduleService;
import com.bm.booking.service.SeatClassService;
import com.bm.booking.service.StopService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserSearchController {

    private final StopService stopService;
    private final ScheduleService scheduleService;
    private final SeatClassService seatClassService;
    private final BookingService bookingService;
    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;
    private final BusSeatRepository busSeatRepository;

    // ===============================
    // Show Search Page
    // ===============================
    @GetMapping("/search")
    public String showSearchPage(Model model) {
        model.addAttribute("stops", stopService.findAll());
        return "user-search";
    }

    // ===============================
    // Search Schedules
    // ===============================
    @PostMapping("/search")
    public String searchSchedules(@RequestParam Long sourceId,
                                  @RequestParam Long destinationId,
                                  @RequestParam String travelDate,
                                  Model model) {

        model.addAttribute("schedules",
                scheduleService.findSchedules(
                        sourceId,
                        destinationId,
                        LocalDate.parse(travelDate)
                ));

        return "user-search-results";
    }

    // ===============================
    // Show Booking Form
    // ===============================
    @GetMapping("/book/{bookingId}")
    public String showBookingForm(@PathVariable Long bookingId, Model model) {

        Booking booking = bookingService.findById(bookingId);

        long seatCount = busSeatRepository.countByBooking(booking);

        model.addAttribute("booking", booking);
        model.addAttribute("seatCount", seatCount);

        return "user-booking-form";
    }

    // ===============================
    // Handle Booking Submission (MULTIPLE PASSENGERS)
    // ===============================
    @PostMapping("/book")
    public String addPassengers(@RequestParam Long bookingId,
                                @RequestParam List<String> passengerName,
                                @RequestParam List<Integer> age,
                                @RequestParam List<String> gender) {

        Booking booking = bookingService.findById(bookingId);
        long seatCount = busSeatRepository.countByBooking(booking);

        if (passengerName.size() != seatCount) {
            throw new RuntimeException("Passenger count must match selected seats");
        }

        double totalFare = 0;

        for (int i = 0; i < passengerName.size(); i++) {

            Gender selectedGender;
            try {
                selectedGender = Gender.valueOf(gender.get(i).toUpperCase());
            } catch (Exception e) {
                selectedGender = Gender.MALE;
            }

            Passenger passenger = Passenger.builder()
                    .passengerName(passengerName.get(i))
                    .age(age.get(i))
                    .gender(selectedGender)
                    .booking(booking)
                    .build();

            passengerRepository.save(passenger);

            totalFare += booking.getSeatClass().getBaseFare();
        }

        booking.setTotalFare(totalFare);

        return "redirect:/user/payment/" + booking.getId();
    }
    @Transactional
    @GetMapping("/my-bookings")
    public String myBookings(Authentication authentication, Model model) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        model.addAttribute("bookings",
                bookingService.findByUser(user));

        return "user-my-bookings";
    }

}