package com.bm.booking.repository;

import com.bm.booking.entity.SeatClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {

    // Check if class name already exists (case insensitive)
    boolean existsByClassNameIgnoreCase(String className);

    // Get all active seat classes
    List<SeatClass> findByActiveTrue();

    // Optional: Get all inactive seat classes
    //List<SeatClass> findByActiveFalse();




}