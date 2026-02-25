package com.bm.booking.service;

import com.bm.booking.entity.Schedule;
import com.bm.booking.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }
    public List<Schedule> findSchedules(Long sourceId,
                                        Long destinationId,
                                        LocalDate travelDate) {

        return scheduleRepository
                .findByRoute_SourceStop_IdAndRoute_DestinationStop_IdAndTravelDate(
                        sourceId,
                        destinationId,
                        travelDate
                );
    }
    public Schedule findById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }
}