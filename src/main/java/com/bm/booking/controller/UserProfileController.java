package com.bm.booking.controller;

import com.bm.booking.entity.User;
import com.bm.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // =========================
    // SHOW PROFILE PAGE
    // =========================
    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        model.addAttribute("user", user);

        return "profile";
    }

    // =========================
    // UPDATE PROFILE
    // =========================
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                Authentication authentication) {

        String email = authentication.getName();
        User existingUser = userService.findByEmail(email);

        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setPhone(updatedUser.getPhone());

        userService.save(existingUser);

        return "redirect:/profile";
    }

    // =========================
    // SHOW CHANGE PASSWORD PAGE
    // =========================
    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "change-password";
    }

    // =========================
    // UPDATE PASSWORD
    // =========================
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication authentication,
                                 Model model) {

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Current password is incorrect");
            return "change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "change-password";
        }

        String passwordPattern =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";

        if (!newPassword.matches(passwordPattern)) {
            model.addAttribute("error",
                    "Password must contain uppercase, lowercase, number, special character & 8+ chars");
            return "change-password";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);

        // ✅ REDIRECT TO DASHBOARD
        return "redirect:/user/dashboard";
    }
}