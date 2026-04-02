package com.bm.booking.service;

import com.bm.booking.entity.*;
import com.bm.booking.repository.BusSeatRepository;
import com.bm.booking.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BusSeatRepository busSeatRepository;

    //  Razorpay Keys (from application.properties)
    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    // ============================
    //  1. CREATE RAZORPAY ORDER
    // ============================
    public String createRazorpayOrder(Booking booking) throws Exception {

        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", booking.getTotalFare() * 100); // convert to paise
        options.put("currency", "INR");
        options.put("receipt", "order_" + booking.getId());

        Order order = client.orders.create(options);

        return order.toString(); // send to frontend
    }

    // ============================
    // ✅ 2. PROCESS PAYMENT
    // ============================
    @Transactional
    public Payment processPayment(Booking booking, PaymentMethod method) {

        PaymentStatus status;

        if (method == PaymentMethod.UPI) {
            status = PaymentStatus.SUCCESS;
        } else {
            status = PaymentStatus.PENDING_CASH;
        }

        // ✅ Update booking status
        booking.setStatus(BookingStatus.CONFIRMED);

        // ✅ Convert seats to BOOKED
        List<BusSeat> seats = busSeatRepository.findByBooking(booking);

        for (BusSeat seat : seats) {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setLockedAt(null);
        }

        // ✅ Save payment
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalFare())
                .method(method)
                .status(status)
                .build();

        return paymentRepository.save(payment);
    }
    @Transactional
    public void cancelBooking(Booking booking) throws Exception {

        if (booking.getSchedule().getTravelDate().isBefore(java.time.LocalDate.now())) {
            throw new RuntimeException("Cannot cancel past booking");
        }

        Payment payment = paymentRepository.findByBooking(booking);

        // ✅ SAFE REFUND LOGIC
        if (payment != null
                && payment.getMethod() == PaymentMethod.UPI
                && payment.getStatus() == PaymentStatus.SUCCESS
                && payment.getRazorpayPaymentId() != null) {

            try {
                RazorpayClient client = new RazorpayClient(keyId, keySecret);

                JSONObject refundRequest = new JSONObject();
                refundRequest.put("amount", booking.getTotalFare() * 100);

                client.payments.refund(payment.getRazorpayPaymentId(), refundRequest);

                payment.setStatus(PaymentStatus.REFUNDED);

            } catch (Exception e) {
                System.out.println("Refund failed: " + e.getMessage());
            }
        }

        // ✅ Always cancel booking
        booking.setStatus(BookingStatus.CANCELLED);

        // ✅ Unlock seats
        List<BusSeat> seats = busSeatRepository.findByBooking(booking);

        for (BusSeat seat : seats) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setBooking(null);
            seat.setLockedAt(null);
        }
    }
    @Transactional
    public void saveOnlinePayment(Booking booking, String paymentId) {

        // Update booking
        booking.setStatus(BookingStatus.CONFIRMED);

        // Lock seats as BOOKED
        List<BusSeat> seats = busSeatRepository.findByBooking(booking);

        for (BusSeat seat : seats) {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setLockedAt(null);
        }

        // Save payment with Razorpay ID
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalFare())
                .method(PaymentMethod.UPI)
                .status(PaymentStatus.SUCCESS)
                .razorpayPaymentId(paymentId) //
                .build();

        paymentRepository.save(payment);
    }
}