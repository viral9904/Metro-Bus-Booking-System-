package com.bm.booking.repository;

import com.bm.booking.entity.SeatClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {
}