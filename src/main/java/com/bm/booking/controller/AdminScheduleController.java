package com.bm.booking.controller;

import com.bm.booking.entity.Schedule;
import com.bm.booking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin/schedules")
@RequiredArgsConstructor
public class AdminScheduleController {

    private final ScheduleService scheduleService;
    private final RouteService routeService;
    private final VehicleService vehicleService;

    @GetMapping
    public String listSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.findAll());
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("routes", routeService.findAll());
        model.addAttribute("vehicles", vehicleService.findAll());
        return "admin-schedules";
    }

    @PostMapping("/save")
    public String saveSchedule(@RequestParam Long routeId,
                               @RequestParam Long vehicleId,
                               @RequestParam LocalDate travelDate,
                               @RequestParam LocalTime departureTime,
                               @RequestParam LocalTime arrivalTime,
                               @RequestParam(required = false, defaultValue = "1") int repeatDays,
                               Model model) {

        try {

            for (int i = 0; i < repeatDays; i++) {

                LocalDate date = travelDate.plusDays(i);

                try {
                    scheduleService.createSchedule(
                            routeId,
                            vehicleId,
                            date,
                            departureTime,
                            arrivalTime
                    );
                } catch (RuntimeException e) {
                    // Skip duplicates but continue
                    continue;
                }
            }

        } catch (Exception e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("schedules", scheduleService.findAll());
            model.addAttribute("routes", routeService.findAll());
            model.addAttribute("vehicles", vehicleService.findAll());
            model.addAttribute("schedule", new Schedule());

            return "admin-schedules";
        }

        return "redirect:/admin/schedules";
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Long id, Model model) {

        try {
            scheduleService.delete(id);
        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("schedules", scheduleService.findAll());
            model.addAttribute("routes", routeService.findAll());
            model.addAttribute("vehicles", vehicleService.findAll());
            model.addAttribute("schedule", new Schedule());

            return "admin-schedules";
        }

        return "redirect:/admin/schedules";
    }
    @GetMapping("/generate-schedules")
    public String generateSchedules() {

        scheduleService.generateSmartSchedules(30);

        return "redirect:/admin/dashboard";
    }
}