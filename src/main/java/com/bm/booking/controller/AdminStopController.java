package com.bm.booking.controller;

import com.bm.booking.entity.Stop;
import com.bm.booking.service.StopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/stops")
@RequiredArgsConstructor
public class AdminStopController {

    private final StopService stopService;

    @GetMapping
    public String listStops(Model model) {
        model.addAttribute("stops", stopService.findAll());
        model.addAttribute("stop", new Stop()); // ✅ Required for form binding
        return "admin-stops";
    }

    @PostMapping("/save")
    public String saveStop(@RequestParam String city,
                           @RequestParam String stopName,
                           Model model) {

        city = city.trim();
        stopName = stopName.trim();

        if (stopService.existsByCityAndStopName(city, stopName)) {

            model.addAttribute("error",
                    "Stop already exists in this city!");
            model.addAttribute("stops", stopService.findAll());
            model.addAttribute("stop", new Stop());
            return "admin-stops";
        }

        Stop stop = new Stop();
        stop.setCity(city);
        stop.setStopName(stopName);

        stopService.save(stop);

        return "redirect:/admin/stops";
    }
    @GetMapping("/delete/{id}")
    public String deleteStop(@PathVariable Long id) {
        stopService.delete(id);
        return "redirect:/admin/stops";
    }

    @GetMapping("/by-city")
    @ResponseBody
    public List<Stop> getStopsByCity(@RequestParam String city) {
        return stopService.findByCity(city);
    }
}