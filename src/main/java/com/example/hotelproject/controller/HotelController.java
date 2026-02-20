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
import java.util.Optional;

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

    // GET /search?city=minsk
    @GetMapping("/search")
    public List<HotelShortDTO> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenity
    ) {
        log.info("Search hotels with params: name={}, brand={}, city={}, country={}, amenity={}",
                name, brand, city, country, amenity);
        return hotelService.searchHotels(name, brand, city, country, amenity);
    }

    //POST /hotels - создание нового отеля
    @PostMapping("/hotels")
    public HotelDTO createHotel(@RequestBody HotelDTO hotelDTO) {
        log.info("Create hotel with data: " + hotelDTO);
        return hotelService.createHotel(hotelDTO);
    }

    //POST /hotels/{id}/amenities - добавление списка amenities к отелю
    @PostMapping("/hotels/{id}/amenities")
    public Optional<HotelDTO> addAmenities(
            @PathVariable Long id,
            @RequestParam(required = false) List<String> amenities
    ) {
        log.info("Add amenities with params: " + amenities);
        return hotelService.addAmenities(id, amenities);
    }

    //GET /histogram/{param} - получение количества отелей сгруппированных по каждому значению указанного параметра. Параметр: brand, city, country, amenities.
    //	Например: /histogram/city должен вернуть:
    //		{
    //			"Minsk": 1,
    //			"Moskow: 2,
    //			"Mogilev: 1,
    //			и тд.
    //		}
    //	а /histogram/amenities должен вернуть:
    //		{
    //			"Free parking": 1,
    //			"Free WiFi: 20,
    //			"Non-smoking rooms": 5,
    //			"Fitness center": 1,
    //			и тд.
    //		}

    @GetMapping("/histogram/{param}")
    public List<HotelShortDTO> getHotelHistogram(
            @PathVariable String param
    ) {
        log.info("Get hotel histogram with params: " + param);
        return hotelService.getHotelsListGroupByParam(param);
    }

}
