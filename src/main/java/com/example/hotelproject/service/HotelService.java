package com.example.hotelproject.service;

import com.example.hotelproject.dto.HotelDTO;
import com.example.hotelproject.dto.HotelShortDTO;
import com.example.hotelproject.entity.Hotel;
import com.example.hotelproject.mapper.HotelMapper;
import com.example.hotelproject.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    
    //для GET /hotels - краткая информация
    public List<HotelShortDTO> getAllHotelsShortInfo() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotelMapper.toShortDTOList(hotels);
    }
    
    // для GET /hotels/{id} - полная информация
    public HotelDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        return hotelMapper.toDTO(hotel);
    }
}
