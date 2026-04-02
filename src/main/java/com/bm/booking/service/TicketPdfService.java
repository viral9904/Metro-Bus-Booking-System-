package com.bm.booking.service;

import com.bm.booking.entity.Booking;
import com.bm.booking.entity.BusSeat;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketPdfService {

    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(79, 70, 229);
    private static final DeviceRgb LIGHT_BG = new DeviceRgb(241, 245, 249);
    private static final DeviceRgb TEXT_DARK = new DeviceRgb(30, 41, 59);
    private static final DeviceRgb TEXT_MID = new DeviceRgb(71, 85, 105);

    @Transactional
    public byte[] generateTicketPdf(Booking booking, List<BusSeat> seats) {

        if (booking == null) {
            throw new RuntimeException("Booking not found");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc, PageSize.A4);
            doc.setMargins(40, 40, 40, 40);

            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // ✅ SAFE DATA EXTRACTION
            String passengerName = booking.getUser() != null
                    ? booking.getUser().getFullName()
                    : "N/A";

            String vehicleInfo = (booking.getSchedule() != null && booking.getSchedule().getVehicle() != null)
                    ? booking.getSchedule().getVehicle().getVehicleType().name() + " — "
                    + booking.getSchedule().getVehicle().getVehicleNumber()
                    : "N/A";

            String routeFrom = (booking.getSchedule() != null &&
                    booking.getSchedule().getRoute() != null &&
                    booking.getSchedule().getRoute().getSourceStop() != null)
                    ? booking.getSchedule().getRoute().getSourceStop().getStopName()
                    : "N/A";

            String routeTo = (booking.getSchedule() != null &&
                    booking.getSchedule().getRoute() != null &&
                    booking.getSchedule().getRoute().getDestinationStop() != null)
                    ? booking.getSchedule().getRoute().getDestinationStop().getStopName()
                    : "N/A";

            String distance = (booking.getSchedule() != null &&
                    booking.getSchedule().getRoute() != null)
                    ? booking.getSchedule().getRoute().getDistanceKm() + " km"
                    : "N/A";

            String travelDate = booking.getSchedule() != null
                    ? booking.getSchedule().getTravelDate().toString()
                    : "N/A";

            String departure = booking.getSchedule() != null
                    ? booking.getSchedule().getDepartureTime().toString()
                    : "N/A";

            String arrival = booking.getSchedule() != null
                    ? booking.getSchedule().getArrivalTime().toString()
                    : "N/A";

            String seatClass = booking.getSeatClass() != null
                    ? booking.getSeatClass().getClassName()
                    : "N/A";

            String status = booking.getStatus() != null
                    ? booking.getStatus().name()
                    : "N/A";

            // ✅ Seats
            String seatNumbers = (seats == null || seats.isEmpty())
                    ? "Not Assigned"
                    : seats.stream()
                    .map(BusSeat::getSeatNumber)
                    .collect(Collectors.joining(", "));

            // ✅ Fare (FIXED - no null check on double)
            String totalFare = String.format("₹ %.2f", booking.getTotalFare());

            DateTimeFormatter dateTimeFormat =
                    DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

            String bookingDate = booking.getBookingDate() != null
                    ? booking.getBookingDate().format(dateTimeFormat)
                    : "N/A";

            // ================= HEADER =================
            Table headerTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            Cell headerCell = new Cell()
                    .setBackgroundColor(PRIMARY_COLOR)
                    .setPadding(20)
                    .setBorder(Border.NO_BORDER);

            headerCell.add(new Paragraph("BM BOOKING")
                    .setFont(boldFont).setFontSize(24)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER));

            headerCell.add(new Paragraph("E-Ticket / Boarding Pass")
                    .setFont(regularFont).setFontSize(12)
                    .setFontColor(new DeviceRgb(200, 200, 255))
                    .setTextAlignment(TextAlignment.CENTER));

            headerTable.addCell(headerCell);
            doc.add(headerTable);

            // ================= ID BAR =================
            Table idBar = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth().setMarginTop(15);

            idBar.addCell(createInfoCell("Ticket ID",
                    "BM-" + String.format("%06d", booking.getId()),
                    boldFont, regularFont));

            idBar.addCell(createInfoCell("Booking Date",
                    bookingDate,
                    boldFont, regularFont));

            doc.add(idBar);

            // ================= PASSENGER =================
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth()
                    .setBackgroundColor(LIGHT_BG);

            infoTable.addCell(createInfoCell("Passenger Name", passengerName, boldFont, regularFont));
            infoTable.addCell(createInfoCell("Vehicle", vehicleInfo, boldFont, regularFont));

            doc.add(infoTable);

            // ================= ROUTE =================
            doc.add(new Paragraph("Journey Details")
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setFontColor(PRIMARY_COLOR)
                    .setMarginTop(20));

            Table routeTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                    .useAllAvailableWidth();

            routeTable.addCell(createDetailCell("From", routeFrom, boldFont, regularFont));
            routeTable.addCell(createDetailCell("To", routeTo, boldFont, regularFont));
            routeTable.addCell(createDetailCell("Distance", distance, boldFont, regularFont));

            doc.add(routeTable);

            // ================= TIME =================
            Table timeTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                    .useAllAvailableWidth()
                    .setMarginTop(10);

            timeTable.addCell(createDetailCell("Travel Date", travelDate, boldFont, regularFont));
            timeTable.addCell(createDetailCell("Departure", departure, boldFont, regularFont));
            timeTable.addCell(createDetailCell("Arrival", arrival, boldFont, regularFont));

            doc.add(timeTable);

            // ================= SEATS =================
            doc.add(new Paragraph("Seat & Fare Details")
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setFontColor(PRIMARY_COLOR)
                    .setMarginTop(20));

            Table seatTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                    .useAllAvailableWidth();

            seatTable.addCell(createDetailCell("Seat Numbers", seatNumbers, boldFont, regularFont));
            seatTable.addCell(createDetailCell("Seat Class", seatClass, boldFont, regularFont));
            seatTable.addCell(createDetailCell("Total Fare", totalFare, boldFont, regularFont));

            doc.add(seatTable);

            // ================= STATUS =================
            Cell statusCell = new Cell()
                    .setBackgroundColor(new DeviceRgb(209, 250, 229))
                    .setPadding(12)
                    .setBorder(new SolidBorder(new DeviceRgb(16, 185, 129), 1));

            statusCell.add(new Paragraph("Status: " + status)
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Table(1).useAllAvailableWidth().addCell(statusCell));

            // ================= FOOTER =================
            doc.add(new Paragraph("Thank you for choosing BM Booking!")
                    .setFont(regularFont)
                    .setFontSize(10)
                    .setFontColor(TEXT_MID)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(25));

            doc.add(new Paragraph("This is a computer-generated ticket.")
                    .setFont(regularFont)
                    .setFontSize(8)
                    .setFontColor(TEXT_MID)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ticket PDF", e);
        }
    }

    private Cell createInfoCell(String label, String value, PdfFont boldFont, PdfFont regularFont) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER).setPadding(8);
        cell.add(new Paragraph(label).setFont(regularFont).setFontSize(9).setFontColor(TEXT_MID));
        cell.add(new Paragraph(value).setFont(boldFont).setFontSize(12).setFontColor(TEXT_DARK));
        return cell;
    }

    private Cell createDetailCell(String label, String value, PdfFont boldFont, PdfFont regularFont) {
        Cell cell = new Cell()
                .setBorder(new SolidBorder(new DeviceRgb(226, 232, 240), 0.5f))
                .setPadding(10);
        cell.add(new Paragraph(label).setFont(regularFont).setFontSize(9).setFontColor(TEXT_MID));
        cell.add(new Paragraph(value).setFont(boldFont).setFontSize(11).setFontColor(TEXT_DARK));
        return cell;
    }
}