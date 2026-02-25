package com.bm.booking.service;

import com.bm.booking.entity.Stop;
import com.bm.booking.repository.StopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StopService {

    private final StopRepository stopRepository;

    public Stop save(Stop stop) {
        return stopRepository.save(stop);
    }

    public List<Stop> findAll() {
        return stopRepository.findAll();
    }

    public void delete(Long id) {
        stopRepository.deleteById(id);
    }

    public Stop findById(Long id) {
        return stopRepository.findById(id).orElseThrow();
    }
    public List<Stop> findByCity(String city) {
        return stopRepository.findByCity(city);
    }
}