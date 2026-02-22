package com.example.hotelproject.repository;

import com.example.hotelproject.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    Optional<Amenity> findByName(String name);

    @Query("SELECT am.name, COUNT(h) FROM Hotel h " +
            "JOIN h.amenities am " +
            "GROUP BY am.name")
    List<Object[]> groupHotelsByAmenities();
}
