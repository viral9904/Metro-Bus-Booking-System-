package com.bm.booking.repository;

import com.bm.booking.entity.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StopRepository extends JpaRepository<Stop, Long> {

    List<Stop> findByCity(String city);

    boolean existsByCityIgnoreCaseAndStopNameIgnoreCase(String city, String stopName);
}