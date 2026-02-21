package com.example.hotelproject.service;

import com.example.hotelproject.dto.HotelDTO;
import com.example.hotelproject.dto.HotelShortDTO;
import com.example.hotelproject.entity.Hotel;
import com.example.hotelproject.mapper.HotelMapper;
import com.example.hotelproject.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    
    // GET /hotels - краткая информация
    public List<HotelShortDTO> getAllHotelsShortInfo() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotelMapper.toShortDTOList(hotels);
    }
    
    // GET /hotels/{id} - полная информация
    public HotelDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        return hotelMapper.toDTO(hotel);
    }
    
    // GET /search - поиск по параметру (поддержка одного или нескольких значений)
    public List<HotelShortDTO> searchHotels(
            String name,
            List<String> brands,
            List<String> cities,
            List<String> countries,
            List<String> amenities
    ) {
        List<Hotel> hotels;
        
        // Ищем по тому параметру, который передан
        if (name != null && !name.isEmpty()) {
            hotels = hotelRepository.findByNameContainingIgnoreCase(name);
        } 
        else if (brands != null && !brands.isEmpty()) {
            if (brands.size() == 1) {
                hotels = hotelRepository.findByBrand_Name(brands.get(0));
            } else {
                // Преобразуем в верхний регистр для поиска
                List<String> upperBrands = brands.stream().map(String::toUpperCase).toList();
                hotels = hotelRepository.findByBrandNames(upperBrands);
            }
        } 
        else if (cities != null && !cities.isEmpty()) {
            if (cities.size() == 1) {
                hotels = hotelRepository.findByCity(cities.get(0));
            } else {
                // Преобразуем в верхний регистр для поиска
                List<String> upperCities = cities.stream().map(String::toUpperCase).toList();
                hotels = hotelRepository.findByCities(upperCities);
            }
        } 
        else if (countries != null && !countries.isEmpty()) {
            if (countries.size() == 1) {
                hotels = hotelRepository.findByCountry(countries.get(0));
            } else {
                // Преобразуем в верхний регистр для поиска
                List<String> upperCountries = countries.stream().map(String::toUpperCase).toList();
                hotels = hotelRepository.findByCountries(upperCountries);
            }
        } 
        else if (amenities != null && !amenities.isEmpty()) {
            if (amenities.size() == 1) {
                hotels = hotelRepository.findByAmenities_Name(amenities.get(0));
            } else {
                // Преобразуем в верхний регистр для поиска
                List<String> upperAmenities = amenities.stream().map(String::toUpperCase).toList();
                hotels = hotelRepository.findByAnyAmenities(upperAmenities);
            }
        } 
        else {
            // Если параметры не переданы - возвращаем все отели
            hotels = hotelRepository.findAll();
        }
        
        return hotelMapper.toShortDTOList(hotels);
    }
}
