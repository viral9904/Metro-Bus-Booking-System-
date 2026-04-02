package com.bm.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= ROUTE =================
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    // ================= VEHICLE =================
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    // ================= DEPARTURE TIME =================
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    // ================= ARRIVAL TIME =================
    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    // ================= TRAVEL DATE =================
    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    // ================= BUS SEATS =================
    @OneToMany(mappedBy = "schedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BusSeat> seats;
}