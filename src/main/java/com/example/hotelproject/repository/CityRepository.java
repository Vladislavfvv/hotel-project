package com.example.hotelproject.repository;

import com.example.hotelproject.entity.City;
import com.example.hotelproject.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
    Optional<City> findByNameAndCountry(String name, Country country);
    
    List<City> findByCountry(Country country);
    
    List<City> findByName(String name);
}
