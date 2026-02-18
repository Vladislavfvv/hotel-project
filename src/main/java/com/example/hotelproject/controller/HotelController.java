package com.example.hotelproject.controller;

import com.example.hotelproject.dto.HotelDTO;
import com.example.hotelproject.dto.HotelShortDTO;
import com.example.hotelproject.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/property-view/")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    // GET /hotels
    @GetMapping
    public List<HotelShortDTO> getAllHotels() {
        return hotelService.getAllHotelsShortInfo();
    }

    // GET /hotels/{id}
    @GetMapping("/{id}")
    public HotelDTO getHotel(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }


}
