package com.bm.booking.service;

import com.bm.booking.entity.*;
import com.bm.booking.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment processPayment(Booking booking, PaymentMethod method) {

        PaymentStatus status;

        if (method == PaymentMethod.UPI) {
            status = PaymentStatus.SUCCESS;
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            status = PaymentStatus.PENDING_CASH;
            booking.setStatus(BookingStatus.CONFIRMED);
        }

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalFare())
                .method(method)
                .status(status)
                .build();

        return paymentRepository.save(payment);
    }
}