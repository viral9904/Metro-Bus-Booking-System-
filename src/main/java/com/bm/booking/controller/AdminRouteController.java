package com.bm.booking.controller;

import com.bm.booking.entity.Route;
import com.bm.booking.service.RouteService;
import com.bm.booking.service.StopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/routes")
@RequiredArgsConstructor
public class AdminRouteController {

    private final RouteService routeService;
    private final StopService stopService;

    @GetMapping
    public String listRoutes(Model model) {
        model.addAttribute("routes", routeService.findAll());
        model.addAttribute("stops", stopService.findAll());
        return "admin-routes";
    }

    @PostMapping("/save")
    public String saveRoute(@RequestParam Long sourceStop,
                            @RequestParam Long destinationStop,
                            @RequestParam double distanceKm) {

        Route route = new Route();
        route.setSourceStop(stopService.findById(sourceStop));
        route.setDestinationStop(stopService.findById(destinationStop));
        route.setDistanceKm(distanceKm);

        routeService.save(route);

        return "redirect:/admin/routes";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoute(@PathVariable Long id) {
        routeService.delete(id);
        return "redirect:/admin/routes";
    }
}