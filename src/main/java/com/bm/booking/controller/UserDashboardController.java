package com.bm.booking.controller;

import com.bm.booking.entity.Schedule;
import com.bm.booking.entity.Stop;
import com.bm.booking.service.ScheduleService;
import com.bm.booking.service.StopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserDashboardController {

    private final ScheduleService scheduleService;
    private final StopService stopService;

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<Schedule> upcoming = scheduleService.getUpcomingSchedules();
        List<Schedule> allSchedules = scheduleService.findAll();

        model.addAttribute("upcomingSchedules", upcoming);
        model.addAttribute("allSchedules", allSchedules);

        return "user-dashboard";
    }

    // ================= ALL SCHEDULES =================
    @GetMapping("/schedules")
    public String allSchedules(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String date,
            Model model) {

        List<Schedule> schedules = scheduleService.findAll();
        List<Stop> stops = stopService.findAll();

        // REMOVE PAST SCHEDULES
        schedules = schedules.stream()
                .filter(s -> s.getTravelDate() != null &&
                        !s.getTravelDate().isBefore(LocalDate.now()))
                .toList();

        // SOURCE FILTER
        if (source != null && !source.isBlank()) {
            String src = source.toLowerCase();

            schedules = schedules.stream()
                    .filter(s -> s.getRoute().getSourceStop().getStopName()
                            .toLowerCase().contains(src))
                    .toList();
        }

        // DESTINATION FILTER
        if (destination != null && !destination.isBlank()) {
            String dest = destination.toLowerCase();

            schedules = schedules.stream()
                    .filter(s -> s.getRoute().getDestinationStop().getStopName()
                            .toLowerCase().contains(dest))
                    .toList();
        }

        // VEHICLE TYPE FILTER
        if (type != null && !type.isBlank()) {
            schedules = schedules.stream()
                    .filter(s -> s.getVehicle() != null &&
                            s.getVehicle().getVehicleType() != null &&
                            s.getVehicle().getVehicleType().name().equalsIgnoreCase(type))
                    .toList();
        }

        // DATE FILTER
        if (date != null && !date.isBlank()) {
            try {
                LocalDate selectedDate = LocalDate.parse(date);

                schedules = schedules.stream()
                        .filter(s -> s.getTravelDate() != null &&
                                s.getTravelDate().equals(selectedDate))
                        .toList();

            } catch (Exception e) {
                System.out.println("Invalid date format: " + date);
            }
        }

        // 🔥🔥 ADD THIS (MOST IMPORTANT FIX)
        schedules = schedules.stream()
                .sorted(Comparator
                        .comparing(Schedule::getTravelDate)
                        .thenComparing(Schedule::getDepartureTime))
                .toList();

        // SEND DATA TO VIEW
        model.addAttribute("allSchedules", schedules);
        model.addAttribute("stops", stops);

        // KEEP FILTER VALUES
        model.addAttribute("source", source);
        model.addAttribute("destination", destination);
        model.addAttribute("type", type);
        model.addAttribute("date", date);

        return "user-schedules";
    }
}