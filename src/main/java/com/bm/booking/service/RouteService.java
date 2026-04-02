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


    public boolean existsRoute(Long sourceId, Long destId, double distanceKm) {
        return routeRepository
                .existsBySourceStopIdAndDestinationStopIdAndDistanceKm(
                        sourceId, destId, distanceKm);
    }
    public boolean existsByStops(Long sourceId, Long destinationId) {
        return routeRepository
                .existsBySourceStopIdAndDestinationStopId(
                        sourceId, destinationId);
    }
    public Route findById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));
    }
}