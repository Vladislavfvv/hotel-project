package com.example.hotelproject.repository;

import com.example.hotelproject.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Поиск по имени
    Hotel findByName(String name);

    Optional<Hotel> findByNameIs(String name);


    // Найти отели, содержащие текст в названии
    List<Hotel> findByNameContainingIgnoreCase(String name);


    // Найти все отели бренда
    @Query("SELECT h FROM Hotel h WHERE UPPER(h.brand.name) = UPPER(:name)")
    List<Hotel> findByBrand_Name(@Param("name") String name);

    // Найти отели нескольких брендов
    @Query("SELECT h FROM Hotel h WHERE UPPER(h.brand.name) IN :brandNames")
    List<Hotel> findByBrandNames(@Param("brandNames") List<String> brandNames);


    //  Поиск по месторасположению
    // Найти отели в городе
    @Query("SELECT h FROM Hotel h WHERE UPPER(h.address.street.city.name) = UPPER(:cityName)")
    List<Hotel> findByCity(@Param("cityName") String cityName);

    // Найти отели в нескольких городах
    @Query("SELECT h FROM Hotel h WHERE UPPER(h.address.street.city.name) IN :cityNames")
    List<Hotel> findByCities(@Param("cityNames") List<String> cityNames);

    // Найти отели в стране
    @Query("SELECT h FROM Hotel h WHERE UPPER(h.address.street.city.country.name) = UPPER(:countryName)")
    List<Hotel> findByCountry(@Param("countryName") String countryName);

    // Найти отели в нескольких странах
    @Query("SELECT h FROM Hotel h WHERE UPPER(h.address.street.city.country.name) IN :countryNames")
    List<Hotel> findByCountries(@Param("countryNames") List<String> countryNames);

    //  Поиск по amenities

    // Найти отели с конкретным удобством
    @Query("SELECT h FROM Hotel h JOIN h.amenities a WHERE UPPER(a.name) = UPPER(:name)")
    List<Hotel> findByAmenities_Name(@Param("name") String name);

    // Найти отели с любым из указанных удобств
    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.amenities a WHERE UPPER(a.name) IN :amenityNames")
    List<Hotel> findByAnyAmenities(@Param("amenityNames") List<String> amenityNames);
}


