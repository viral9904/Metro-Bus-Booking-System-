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
        model.addAttribute("stop", new Stop());
        return "admin-stops";
    }

    @PostMapping("/save")
    public String saveStop(@ModelAttribute Stop stop) {
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