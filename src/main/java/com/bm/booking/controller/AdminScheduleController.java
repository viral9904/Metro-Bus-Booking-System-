package com.bm.booking.controller;

import com.bm.booking.entity.Schedule;
import com.bm.booking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                               @RequestParam String travelDate,
                               @RequestParam String departureTime,
                               @RequestParam String arrivalTime) {

        Schedule schedule = new Schedule();

        schedule.setRoute(routeService.findAll()
                .stream()
                .filter(r -> r.getId().equals(routeId))
                .findFirst()
                .orElseThrow());

        schedule.setVehicle(vehicleService.findAll()
                .stream()
                .filter(v -> v.getId().equals(vehicleId))
                .findFirst()
                .orElseThrow());

        schedule.setTravelDate(java.time.LocalDate.parse(travelDate));
        schedule.setDepartureTime(java.time.LocalTime.parse(departureTime));
        schedule.setArrivalTime(java.time.LocalTime.parse(arrivalTime));

        scheduleService.save(schedule);

        return "redirect:/admin/schedules";
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return "redirect:/admin/schedules";
    }
}