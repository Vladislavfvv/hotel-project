package com.example.hotelproject.repository;

import com.example.hotelproject.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByName(String name);

    @Query("SELECT s.city.country.name, COUNT(h) FROM Hotel h " +
            "JOIN h.address a " +
            "JOIN a.street s " +
            "GROUP BY s.city.country.name")
    List<Object[]> groupHotelsByCountry();
}
