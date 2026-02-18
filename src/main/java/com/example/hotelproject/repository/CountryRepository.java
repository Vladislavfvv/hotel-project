package com.example.hotelproject.repository;

import com.example.hotelproject.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    
    Optional<Country> findByName(String name);

}
