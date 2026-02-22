package com.example.hotelproject.repository;

import com.example.hotelproject.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findByName(String name);

    @Query("SELECT h.brand.name, COUNT(h) FROM Hotel h " +
            "GROUP BY h.brand.name")
    List<Object[]> groupHotelsByBrands();
}
