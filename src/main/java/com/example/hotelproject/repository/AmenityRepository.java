package com.example.hotelproject.repository;

import com.example.hotelproject.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    
    // Найти amenity по имени
    Optional<Amenity> findByName(String name);
}
