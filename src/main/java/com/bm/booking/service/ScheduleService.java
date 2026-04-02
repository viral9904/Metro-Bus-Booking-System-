package com.bm.booking.service;

import com.bm.booking.entity.*;
import com.bm.booking.repository.BusSeatRepository;
import com.bm.booking.repository.ScheduleRepository;
import com.bm.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final RouteService routeService;
    private final VehicleService vehicleService;
    private final BusSeatRepository busSeatRepository;
    private final BookingRepository bookingRepository;

    // ===============================
    // Get All Schedules (SORTED ✅)
    // ===============================
    public List<Schedule> findAll() {
        return scheduleRepository.findAllByOrderByTravelDateAscDepartureTimeAsc();
    }

    // ===============================
    // Save Schedule + Auto Create Seats
    // ===============================
    @Transactional
    public Schedule save(Schedule schedule) {

        Schedule savedSchedule = scheduleRepository.save(schedule);

        // prevent duplicate seats
        createSeatsForSchedule(savedSchedule);

        return savedSchedule;
    }

    // ===============================
    // Create Seats (SAFE ✅)
    // ===============================
    @Transactional
    public void createSeatsForSchedule(Schedule schedule) {

        // avoid duplicate seat creation
        if (busSeatRepository.existsBySchedule(schedule)) {
            return;
        }

        int totalSeats = schedule.getVehicle().getTotalSeats();

        for (int i = 1; i <= totalSeats; i++) {

            BusSeat seat = BusSeat.builder()
                    .seatNumber(String.valueOf(i))
                    .status(SeatStatus.AVAILABLE)
                    .schedule(schedule)
                    .build();

            busSeatRepository.save(seat);
        }
    }

    // ===============================
    // Delete Schedule
    // ===============================
    @Transactional
    public void delete(Long id) {

        Schedule schedule = findById(id);

        boolean hasBookings = bookingRepository.existsBySchedule_Id(id);

        if (hasBookings) {
            throw new RuntimeException(
                    "Cannot delete schedule. Bookings already exist for this schedule.");
        }

        scheduleRepository.delete(schedule);
    }

    // ===============================
    // Check Duplicate Schedule
    // ===============================
    public boolean existsSchedule(Long routeId,
                                  Long vehicleId,
                                  LocalDate travelDate,
                                  LocalTime departureTime) {

        return scheduleRepository
                .existsByRoute_IdAndVehicle_IdAndTravelDateAndDepartureTime(
                        routeId, vehicleId, travelDate, departureTime);
    }

    // ===============================
    // Create Schedule
    // ===============================
    @Transactional
    public Schedule createSchedule(Long routeId,
                                   Long vehicleId,
                                   LocalDate travelDate,
                                   LocalTime departureTime,
                                   LocalTime arrivalTime) {

        if (existsSchedule(routeId, vehicleId, travelDate, departureTime)) {
            throw new RuntimeException(
                    "Schedule already exists for this route, vehicle and time!");
        }

        Route route = routeService.findById(routeId);
        Vehicle vehicle = vehicleService.findById(vehicleId);

        Schedule schedule = Schedule.builder()
                .route(route)
                .vehicle(vehicle)
                .travelDate(travelDate)
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .build();

        return save(schedule);
    }

    // ===============================
    // Search Schedules
    // ===============================
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

    // ===============================
    // Find By ID
    // ===============================
    public Schedule findById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    // ===============================
    // Get Seats + Auto Unlock
    // ===============================
    @Transactional
    public List<BusSeat> getSeatsBySchedule(Long scheduleId) {

        Schedule schedule = findById(scheduleId);
        List<BusSeat> seats = busSeatRepository.findBySchedule(schedule);

        LocalDateTime now = LocalDateTime.now();

        for (BusSeat seat : seats) {

            if (seat.getStatus() == SeatStatus.LOCKED &&
                    seat.getLockedAt() != null &&
                    seat.getLockedAt().isBefore(now.minusMinutes(5))) {

                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockedAt(null);
            }
        }

        return seats;
    }

    // ===============================
    // Auto Unlock Scheduler
    // ===============================
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void autoUnlockExpiredSeats() {

        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(5);

        List<BusSeat> expiredSeats =
                busSeatRepository.findByStatusAndLockedAtBefore(
                        SeatStatus.LOCKED, expiryTime);

        for (BusSeat seat : expiredSeats) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedAt(null);
        }

        if (!expiredSeats.isEmpty()) {
            System.out.println("Auto unlocked seats: " + expiredSeats.size());
        }
    }

    // ===============================
    // Upcoming Schedules
    // ===============================
    public List<Schedule> getUpcomingSchedules() {
        return scheduleRepository.findAllByOrderByTravelDateAscDepartureTimeAsc()
                .stream()
                .limit(10)
                .toList();
    }

    // ===============================
    // Smart Generator
    // ===============================
    @Transactional
    public void generateSmartSchedules(int days) {

        List<Route> routes = routeService.findAll();
        List<Vehicle> vehicles = vehicleService.findAll();

        for (Route route : routes) {

            double distance = route.getDistance();
            int speed = 30;
            int travelMinutes = (int) Math.ceil((distance * 60) / speed);

            for (Vehicle vehicle : vehicles) {

                for (int d = 0; d < days; d++) {

                    LocalDate date = LocalDate.now().plusDays(d);
                    LocalTime departure = LocalTime.of(6, 0);

                    for (int i = 0; i < 40; i++) {

                        if (existsSchedule(
                                route.getId(),
                                vehicle.getId(),
                                date,
                                departure)) {

                            departure = departure.plusMinutes(15);
                            continue;
                        }

                        LocalTime arrival = departure.plusMinutes(travelMinutes);

                        Schedule schedule = Schedule.builder()
                                .route(route)
                                .vehicle(vehicle)
                                .travelDate(date)
                                .departureTime(departure)
                                .arrivalTime(arrival)
                                .build();

                        save(schedule);

                        departure = departure.plusMinutes(15);
                    }
                }
            }
        }
    }
}