package com.example.hotelproject.repository;

import com.example.hotelproject.entity.ArrivalTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrivalTimeRepository extends JpaRepository<ArrivalTime,Long> {
}
