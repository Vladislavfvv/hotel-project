package com.example.hotelproject.repository;

import com.example.hotelproject.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(String name);

    @Query("SELECT s.city.name, COUNT(h) FROM Hotel h " +
            "JOIN h.address a " +
            "JOIN a.street s " +
            "GROUP BY s.city.name")
    List<Object[]> groupHotelsByCities();
}
