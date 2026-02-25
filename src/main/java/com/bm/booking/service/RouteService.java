package com.bm.booking.service;

import com.bm.booking.entity.Route;
import com.bm.booking.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public Route save(Route route) {
        return routeRepository.save(route);
    }

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public void delete(Long id) {
        routeRepository.deleteById(id);
    }
}