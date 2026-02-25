package com.bm.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat_classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name", nullable = false, unique = true)
    private String className;

    @Column(name = "base_fare", nullable = false)
    private double baseFare;
}