package com.bm.booking.controller;

import com.bm.booking.entity.Vehicle;
import com.bm.booking.entity.VehicleType;
import com.bm.booking.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/vehicles")
@RequiredArgsConstructor
public class AdminVehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public String listVehicles(Model model) {
        model.addAttribute("vehicles", vehicleService.findAll());
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("types", VehicleType.values());
        return "admin-vehicles";
    }

    @PostMapping("/save")
    public String saveVehicle(@ModelAttribute Vehicle vehicle) {
        vehicleService.save(vehicle);
        return "redirect:/admin/vehicles";
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleService.delete(id);
        return "redirect:/admin/vehicles";
    }
}