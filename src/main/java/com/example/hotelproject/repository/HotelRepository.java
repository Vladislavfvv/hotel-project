package com.example.hotelproject.repository;

import com.example.hotelproject.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Поиск по имени
    Hotel findByName(String name);

    // Найти отели, содержащие текст в названии
    List<Hotel> findByNameContainingIgnoreCase(String name);


    // Найти все отели бренда
    List<Hotel> findByBrand_Name(String name);

    // Найти отели нескольких брендов
    @Query("SELECT h FROM Hotel h WHERE h.brand.name IN :brandNames")
    List<Hotel> findByBrandNames(@Param("brandNames") List<String> brandNames);


    //  Поиск по месторасположению

    // Найти отели в городе
    @Query("SELECT h FROM Hotel h WHERE h.address.street.city.name = :cityName")
    List<Hotel> findByCity(@Param("cityName") String cityName);

    // Найти отели в нескольких городах
    @Query("SELECT h FROM Hotel h WHERE h.address.street.city.name IN :cityNames")
    List<Hotel> findByCities(@Param("cityNames") List<String> cityNames);

    // Найти отели в стране
    @Query("SELECT h FROM Hotel h WHERE h.address.street.city.country.name = :countryName")
    List<Hotel> findByCountry(@Param("countryName") String countryName);

    // Найти отели в нескольких странах
    @Query("SELECT h FROM Hotel h WHERE h.address.street.city.country.name IN :countryNames")
    List<Hotel> findByCountries(@Param("countryNames") List<String> countryNames);

    // Найти отели по почтовому индексу
    @Query("SELECT h FROM Hotel h WHERE h.address.postcode = :postcode")
    List<Hotel> findByPostcode(@Param("postcode") String postcode);

    //  Поиск по amenities

    // Найти отели с конкретным удобством
    List<Hotel> findByAmenities_Name(String name);

    // Найти отели с любым из указанных удобств
    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.amenities a WHERE a.name IN :amenityNames")
    List<Hotel> findByAnyAmenities(@Param("amenityNames") List<String> amenityNames);


    // !!!!!!!!!!!!!!!!!!!!!!! Комбинированные поиски  - их потом по необходимости использовать в сервис!!!!!!!!!!!!!!!!!!!

    // Отели бренда в городе
    @Query("SELECT h FROM Hotel h WHERE h.brand.name = :brandName AND h.address.street.city.name = :cityName")
    List<Hotel> findByBrandAndCity(@Param("brandName") String brandName, @Param("cityName") String cityName);

    // Отели бренда в стране
    @Query("SELECT h FROM Hotel h WHERE h.brand.name = :brandName AND h.address.street.city.country.name = :countryName")
    List<Hotel> findByBrandAndCountry(@Param("brandName") String brandName, @Param("countryName") String countryName);

    // Отели в городе с удобством
    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.amenities a WHERE h.address.street.city.name = :cityName AND a.name = :amenityName")
    List<Hotel> findByCityAndAmenity(@Param("cityName") String cityName, @Param("amenityName") String amenityName);

    // Отели бренда с удобством
    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.amenities a WHERE h.brand.name = :brandName AND a.name = :amenityName")
    List<Hotel> findByBrandAndAmenity(@Param("brandName") String brandName, @Param("amenityName") String amenityName);
}


