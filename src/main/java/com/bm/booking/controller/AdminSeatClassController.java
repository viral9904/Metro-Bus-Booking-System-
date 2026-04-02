package com.bm.booking.controller;

import com.bm.booking.entity.SeatClass;
import com.bm.booking.service.SeatClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/seat-classes")
@RequiredArgsConstructor
public class AdminSeatClassController {

    private final SeatClassService seatClassService;

    // ============================
    // LIST PAGE
    // ============================
    @GetMapping
    public String listSeatClasses(Model model) {
        model.addAttribute("seatClasses", seatClassService.findAll());
        return "admin-seat-classes";
    }

    // ============================
    // ADD PAGE
    // ============================
    @GetMapping("/add")
    public String addSeatClassForm(Model model) {
        model.addAttribute("seatClass", new SeatClass());
        return "admin-seat-class-form";
    }

    // ============================
    // EDIT PAGE
    // ============================
    @GetMapping("/edit/{id}")
    public String editSeatClass(@PathVariable Long id, Model model) {
        SeatClass seatClass = seatClassService.findById(id);
        model.addAttribute("seatClass", seatClass);
        return "admin-seat-class-form";
    }

    // ============================
    // SAVE (Insert + Update)
    // ============================
    @PostMapping("/save")
    public String saveSeatClass(@ModelAttribute SeatClass seatClass,
                                RedirectAttributes redirectAttributes) {

        if (seatClass.getId() == null &&
                seatClassService.existsByClassName(seatClass.getClassName())) {

            redirectAttributes.addFlashAttribute("error",
                    "Seat class already exists!");
            return "redirect:/admin/seat-classes";
        }

        boolean isUpdate = seatClass.getId() != null;

        seatClassService.save(seatClass);

        if (isUpdate) {
            redirectAttributes.addFlashAttribute("success",
                    "Seat class updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("success",
                    "Seat class added successfully!");
        }

        return "redirect:/admin/seat-classes";
    }
    // ============================
    // DELETE
    // ============================
    @GetMapping("/delete/{id}")
    public String deleteSeatClass(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {

        seatClassService.delete(id);
        redirectAttributes.addFlashAttribute("success",
                "Seat class deleted successfully!");

        return "redirect:/admin/seat-classes";
    }

    // ============================
    // RESTORE
    // ============================
    @GetMapping("/restore/{id}")
    public String restoreSeatClass(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {

        seatClassService.restore(id);
        redirectAttributes.addFlashAttribute("success",
                "Seat class restored successfully!");

        return "redirect:/admin/seat-classes";
    }
}