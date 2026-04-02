package com.bm.booking.controller;

import com.bm.booking.entity.Booking;
import com.bm.booking.entity.BusSeat;
import com.bm.booking.repository.BusSeatRepository;
import com.bm.booking.service.BookingService;
import com.bm.booking.service.TicketPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final BookingService bookingService;
    private final TicketPdfService ticketPdfService;
    private final BusSeatRepository busSeatRepository;

    @GetMapping("/download/{bookingId}")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long bookingId) {

        Booking booking = bookingService.findById(bookingId);
        List<BusSeat> seats = busSeatRepository.findByBooking(booking);

        byte[] pdfBytes = ticketPdfService.generateTicketPdf(booking, seats);

        String filename = "BM-Ticket-" + String.format("%06d", booking.getId()) + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }
}
