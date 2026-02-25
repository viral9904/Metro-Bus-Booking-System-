package com.bm.booking.service;

import com.bm.booking.entity.SeatClass;
import com.bm.booking.repository.SeatClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatClassService {

    private final SeatClassRepository seatClassRepository;

    public SeatClass save(SeatClass seatClass) {
        return seatClassRepository.save(seatClass);
    }

    public List<SeatClass> findAll() {
        return seatClassRepository.findAll();
    }

    public void delete(Long id) {
        seatClassRepository.deleteById(id);
    }
    public SeatClass findById(Long id) {
        return seatClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat class not found"));
    }
}