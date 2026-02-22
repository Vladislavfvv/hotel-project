package com.example.hotelproject.controller;

import com.example.hotelproject.dto.HotelDTO;
import com.example.hotelproject.dto.HotelShortDTO;
import com.example.hotelproject.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    // GET /hotels
    @GetMapping("/hotels")
    public List<HotelShortDTO> getAllHotels() {
        return hotelService.getAllHotelsShortInfo();
    }

    // GET /hotels/{id}
    @GetMapping("/hotels/{id}")
    public HotelDTO getHotel(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    // GET /search?city=minsk или /search?country=Belarus&country=Russia
    @GetMapping("/search")
    public List<HotelShortDTO> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> brand,
            @RequestParam(required = false) List<String> city,
            @RequestParam(required = false) List<String> country,
            @RequestParam(required = false) List<String> amenity
    ) {
        log.info("Search hotels with params: name={}, brand={}, city={}, country={}, amenity={}",
                name, brand, city, country, amenity);
        return hotelService.searchHotels(name, brand, city, country, amenity);
    }

    // POST /hotels - создание нового отеля
    @PostMapping("/hotels")
    public HotelShortDTO createHotel(@RequestBody HotelDTO hotelDTO) {
        log.info("Create hotel with data: {}", hotelDTO);
        return hotelService.createHotel(hotelDTO);
    }

    // POST /hotels/{id}/amenities - добавление списка amenities к отелю
    @PostMapping("/hotels/{id}/amenities")
    public HotelDTO addAmenities(
            @PathVariable Long id,
            @RequestBody List<String> amenities
    ) {
        log.info("Add amenities to hotel id={}: {}", id, amenities);
        return hotelService.addAmenities(id, amenities);
    }

    //GET /histogram/{param} - получение количества отелей сгруппированных по каждому значению указанного параметра. Параметр: brand, city, country, amenities.
    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHotelHistogram(
            @PathVariable String param
    ) {
        log.info("Get grouping list from controller with params: " + param);
        return hotelService.getHotelListGroupByParam(param);
    }

}
