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
    
    // GET /search - поиск по параметру
    public List<HotelShortDTO> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            String amenity
    ) {
        List<Hotel> hotels;
        
        // Ищем по тому параметру, который передан
        if (name != null && !name.isEmpty()) {
            hotels = hotelRepository.findByNameContainingIgnoreCase(name);
        } 
        else if (brand != null && !brand.isEmpty()) {
            hotels = hotelRepository.findByBrand_Name(brand);
        } 
        else if (city != null && !city.isEmpty()) {
            hotels = hotelRepository.findByCity(city);
        } 
        else if (country != null && !country.isEmpty()) {
            hotels = hotelRepository.findByCountry(country);
        } 
        else if (amenity != null && !amenity.isEmpty()) {
            hotels = hotelRepository.findByAmenities_Name(amenity);
        } 
        else {
            // Если параметры не переданы - возвращаем все отели
            hotels = hotelRepository.findAll();
        }
        
        return hotelMapper.toShortDTOList(hotels);
    }
}
