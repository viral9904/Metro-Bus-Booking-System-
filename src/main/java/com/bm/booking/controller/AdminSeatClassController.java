package com.bm.booking.controller;

import com.bm.booking.entity.SeatClass;
import com.bm.booking.service.SeatClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/seat-classes")
@RequiredArgsConstructor
public class AdminSeatClassController {

    private final SeatClassService seatClassService;

    @GetMapping
    public String listSeatClasses(Model model) {
        model.addAttribute("seatClasses", seatClassService.findAll());
        model.addAttribute("seatClass", new SeatClass());
        return "admin-seat-classes";
    }

    @PostMapping("/save")
    public String saveSeatClass(@ModelAttribute SeatClass seatClass) {
        seatClassService.save(seatClass);
        return "redirect:/admin/seat-classes";
    }

    @GetMapping("/delete/{id}")
    public String deleteSeatClass(@PathVariable Long id) {
        seatClassService.delete(id);
        return "redirect:/admin/seat-classes";
    }
}