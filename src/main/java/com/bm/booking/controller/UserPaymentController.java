package com.bm.booking.controller;

import com.bm.booking.entity.*;
import com.bm.booking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserPaymentController {

    private final BookingService bookingService;
    private final PaymentService paymentService;

    @GetMapping("/payment/{bookingId}")
    public String showPaymentPage(@PathVariable Long bookingId,
                                  Model model) {

        Booking booking = bookingService.findById(bookingId);
        model.addAttribute("booking", booking);
        model.addAttribute("methods", PaymentMethod.values());

        return "user-payment";
    }

    @PostMapping("/payment")
    public String processPayment(@RequestParam Long bookingId,
                                 @RequestParam PaymentMethod method) {

        Booking booking = bookingService.findById(bookingId);

        paymentService.processPayment(booking, method);

        return "redirect:/user/confirmation/" + bookingId;
    }

    @GetMapping("/confirmation/{bookingId}")
    public String showConfirmation(@PathVariable Long bookingId,
                                   Model model) {

        Booking booking = bookingService.findById(bookingId);
        model.addAttribute("booking", booking);

        return "user-confirmation";
    }
}