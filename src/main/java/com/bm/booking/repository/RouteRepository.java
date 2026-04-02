package com.bm.booking.repository;

import com.bm.booking.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {

    boolean existsBySourceStopIdAndDestinationStopIdAndDistanceKm(
            Long sourceStopId,
            Long destinationStopId,
            double distanceKm
    );
    boolean existsBySourceStopIdAndDestinationStopId(
            Long sourceStopId,
            Long destinationStopId
    );
}