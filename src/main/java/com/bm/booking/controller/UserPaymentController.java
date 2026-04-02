package com.bm.booking.controller;

import com.bm.booking.entity.*;
import com.bm.booking.repository.BusSeatRepository;
import com.bm.booking.service.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.bm.booking.service.TicketPdfService;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserPaymentController {

    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final BusSeatRepository busSeatRepository;
    private final TicketPdfService ticketPdfService;

    // ✅ Show payment page
    @GetMapping("/payment/{bookingId}")
    public String showPaymentPage(@PathVariable Long bookingId,
                                  Model model) {

        Booking booking = bookingService.findById(bookingId);
        model.addAttribute("booking", booking);
        model.addAttribute("methods", PaymentMethod.values());

        return "user-payment";
    }

    // ✅ POST Payment (existing)
    @PostMapping("/payment")
    public String processPayment(@RequestParam Long bookingId,
                                 @RequestParam PaymentMethod method) {

        Booking booking = bookingService.findById(bookingId);
        paymentService.processPayment(booking, method);

        return "redirect:/user/confirmation/" + bookingId;
    }

    // ✅ NEW: GET Payment for CASH (as you requested)
    @GetMapping("/payment")
    public String processCashPayment(@RequestParam Long bookingId,
                                     @RequestParam PaymentMethod method) {

        Booking booking = bookingService.findById(bookingId);
        paymentService.processPayment(booking, method);

        return "redirect:/user/confirmation/" + bookingId;
    }

    // ✅ Razorpay Order Creation
    @GetMapping("/create-order/{bookingId}")
    @ResponseBody
    public String createOrder(@PathVariable Long bookingId) throws Exception {

        Booking booking = bookingService.findById(bookingId);
        return paymentService.createRazorpayOrder(booking);
    }

    // ✅ Confirmation Page
    @GetMapping("/confirmation/{bookingId}")
    public String showConfirmation(@PathVariable Long bookingId,
                                   Model model) {

        Booking booking = bookingService.findById(bookingId);
        List<BusSeat> seats = busSeatRepository.findByBooking(booking);

        model.addAttribute("booking", booking);
        model.addAttribute("seats", seats);

        return "user-confirmation";
    }
    @GetMapping("/ticket/{bookingId}")
    public void downloadTicket(@PathVariable Long bookingId,
                               HttpServletResponse response) throws Exception {

        Booking booking = bookingService.findById(bookingId);
        List<BusSeat> seats = busSeatRepository.findByBooking(booking);

        byte[] pdf = ticketPdfService.generateTicketPdf(booking, seats);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=ticket_" + booking.getId() + ".pdf");

        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }
    @GetMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) throws Exception {

        Booking booking = bookingService.findById(bookingId);

        paymentService.cancelBooking(booking);

        return "redirect:/user/dashboard";
    }
    @PostMapping("/payment-success")
    @ResponseBody
    public void paymentSuccess(@RequestBody java.util.Map<String, String> data) {

        Long bookingId = Long.parseLong(data.get("bookingId"));
        String paymentId = data.get("paymentId");

        Booking booking = bookingService.findById(bookingId);

        paymentService.saveOnlinePayment(booking, paymentId);
    }
}