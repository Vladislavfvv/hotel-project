package com.example.hotelproject.repository;

import com.example.hotelproject.entity.Brand;
import com.example.hotelproject.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    // Поиск по amenities
    List<Hotel> findByAmenities_Name(String amenityName);
    
    List<Hotel> findByAmenities_NameContaining(String amenityName);
    
    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.amenities a WHERE a.name IN :amenityNames")
    List<Hotel> findByAnyAmenities(@Param("amenityNames") List<String> amenityNames);

    // Поиск по географии
    @Query("SELECT h FROM Hotel h WHERE h.address.street.city.name = :cityName")
    List<Hotel> findByCity(@Param("cityName") String cityName);
    
    @Query("SELECT h FROM Hotel h WHERE h.address.street.city.country.name = :countryName")
    List<Hotel> findByCountry(@Param("countryName") String countryName);
    
    @Query("SELECT h FROM Hotel h WHERE h.address.street.name = :streetName")
    List<Hotel> findByStreet(@Param("streetName") String streetName);
    
    @Query("SELECT h FROM Hotel h WHERE h.address.street.city.name = :cityName AND h.address.street.city.country.name = :countryName")
    List<Hotel> findByCityAndCountry(@Param("cityName") String cityName, @Param("countryName") String countryName);
    
    @Query("SELECT h FROM Hotel h WHERE h.address.postCode = :postCode")
    List<Hotel> findByPostCode(@Param("postCode") String postCode);
    
    // Поиск по бренду
    List<Hotel> findByBrand_Name(String brandName);
}
