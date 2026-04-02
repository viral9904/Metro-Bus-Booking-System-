package com.bm.booking.config;

import com.bm.booking.entity.User;
import com.bm.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserService userService;

    @ModelAttribute("loggedInUserName")
    public String getLoggedInUser(Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {

            String email = authentication.getName();

            if (!email.equals("anonymousUser")) {
                User user = userService.findByEmail(email);

                // ✅ Get FIRST NAME only
                String fullName = user.getFullName();
                return fullName.split(" ")[0];
            }
        }

        return null;
    }
}