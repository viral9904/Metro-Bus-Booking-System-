package com.bm.booking.repository;

import com.bm.booking.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByRoute_SourceStop_IdAndRoute_DestinationStop_IdAndTravelDate(
            Long sourceId,
            Long destinationId,
            LocalDate travelDate
    );
}