package com.bm.booking.service;

import com.bm.booking.dto.UserRegistrationDto;
import com.bm.booking.entity.Role;
import com.bm.booking.entity.User;
import com.bm.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationDto dto) {

        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);
    }
    public void save(User user) {
        userRepository.save(user);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }
}