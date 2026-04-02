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
                            @RequestParam double distanceKm,
                            Model model) {

        var source = stopService.findById(sourceStop);
        var destination = stopService.findById(destinationStop);

        if (routeService.existsByStops(sourceStop, destinationStop)) {

            model.addAttribute("error",
                    "Route already exists between selected stops!");
            model.addAttribute("routes", routeService.findAll());
            model.addAttribute("stops", stopService.findAll());
            return "admin-routes";
        }

        Route route = new Route();
        route.setSourceStop(source);
        route.setDestinationStop(destination);
        route.setDistanceKm(distanceKm);

        routeService.save(route);

        return "redirect:/admin/routes?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoute(@PathVariable Long id) {
        routeService.delete(id);
        return "redirect:/admin/routes";
    }
}