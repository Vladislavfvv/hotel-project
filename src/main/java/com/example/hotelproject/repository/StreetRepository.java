package com.example.hotelproject.repository;

import com.example.hotelproject.entity.City;
import com.example.hotelproject.entity.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {
    
    Optional<Street> findByNameAndCity(String name, City city);
    
    List<Street> findByCity(City city);
    
    List<Street> findByName(String name);
}
