package com.bm.booking.service;

import com.bm.booking.entity.SeatClass;
import com.bm.booking.repository.SeatClassRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatClassService {

    private final SeatClassRepository seatClassRepository;

    // Save
    public SeatClass save(SeatClass seatClass) {
        return seatClassRepository.save(seatClass);
    }

    // Get all (Admin)
    public List<SeatClass> findAll() {
        return seatClassRepository.findAll();
    }

    // Get only active (User side)
    public List<SeatClass> findAllActive() {
        return seatClassRepository.findByActiveTrue();
    }

    // Soft Delete
    @Transactional
    public void delete(Long id) {
        SeatClass seatClass = seatClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat class not found"));

        seatClass.setActive(false);
        seatClassRepository.save(seatClass);
    }

    // Restore
    @Transactional
    public void restore(Long id) {
        SeatClass seatClass = seatClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat class not found"));

        seatClass.setActive(true);
        seatClassRepository.save(seatClass);
    }

    public SeatClass findById(Long id) {
        return seatClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat class not found"));
    }

    public boolean existsByClassName(String className) {
        return seatClassRepository.existsByClassNameIgnoreCase(className);
    }
    public SeatClass getDefaultClass() {
        return seatClassRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Seat Class found"));
    }
}