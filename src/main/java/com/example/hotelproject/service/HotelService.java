package com.example.hotelproject.service;

import com.example.hotelproject.dto.HotelDTO;
import com.example.hotelproject.dto.HotelShortDTO;
import com.example.hotelproject.entity.*;
import com.example.hotelproject.exception.HotelAlreadyExistsException;
import com.example.hotelproject.exception.HotelNotFoundException;
import com.example.hotelproject.mapper.HotelMapper;
import com.example.hotelproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {

    private final HotelRepository hotelRepository;
    private final BrandRepository brandRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final StreetRepository streetRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    // GET /hotels - краткая информация
    public List<HotelShortDTO> getAllHotelsShortInfo() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotelMapper.toShortDTOList(hotels);
    }

    // GET /hotels/{id} - полная информация
    public HotelDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + id));
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
        } else if (brands != null && !brands.isEmpty()) {
            if (brands.size() == 1) {
                hotels = hotelRepository.findByBrand_Name(brands.get(0));
            } else {
                List<String> upperBrands = brands.stream().map(String::toUpperCase).toList();
                hotels = hotelRepository.findByBrandNames(upperBrands);
            }
        } else if (cities != null && !cities.isEmpty()) {
            if (cities.size() == 1) {
                hotels = hotelRepository.findByCity(cities.get(0));
            } else {
                List<String> upperCities = cities.stream().map(String::toUpperCase).toList();
                hotels = hotelRepository.findByCities(upperCities);
            }
        } else if (countries != null && !countries.isEmpty()) {
            if (countries.size() == 1) {
                hotels = hotelRepository.findByCountry(countries.get(0));
            } else {
                List<String> upperCountries = countries.stream().map(String::toUpperCase).toList();
                hotels = hotelRepository.findByCountries(upperCountries);
            }
        } else if (amenities != null && !amenities.isEmpty()) {
            if (amenities.size() == 1) {
                hotels = hotelRepository.findByAmenities_Name(amenities.get(0));
            } else {
                List<String> upperAmenities = amenities.stream().map(String::toUpperCase).toList();
                hotels = hotelRepository.findByAnyAmenities(upperAmenities);
            }
        } else {
            // Если параметры не переданы - возвращаем все отели
            hotels = hotelRepository.findAll();
        }

        return hotelMapper.toShortDTOList(hotels);
    }

    // POST /hotels - создание нового отеля
    @Transactional
    public HotelDTO createHotel(HotelDTO hotelDTO) {
        log.info("Creating hotel: name={}, brand={}", hotelDTO.getName(), hotelDTO.getBrand());

        // Проверка существования отеля
        if (hotelRepository.findByNameIs(hotelDTO.getName()).isPresent()) {
            log.warn("Hotel creation failed: name '{}' already exists", hotelDTO.getName());
            throw new HotelAlreadyExistsException("Hotel '" + hotelDTO.getName() + "' already exists");
        }

        // Создаем базовую сущность Hotel
        Hotel hotel = hotelMapper.toEntity(hotelDTO);

        // Находим или создаем Brand
        Brand brand = brandRepository.findByName(hotelDTO.getBrand())
                .orElseGet(() -> {
                    Brand newBrand = new Brand();
                    newBrand.setName(hotelDTO.getBrand());
                    return brandRepository.save(newBrand);
                });
        hotel.setBrand(brand);

        if (hotelDTO.getAddress() != null) {
            Address address = new Address();
            address.setHouseNumber(hotelDTO.getAddress().getHouseNumber());
            address.setPostcode(hotelDTO.getAddress().getPostCode());

            Country country = countryRepository.findByName(hotelDTO.getAddress().getCountry())
                    .orElseGet(() -> {
                        Country newCountry = new Country();
                        newCountry.setName(hotelDTO.getAddress().getCountry());
                        return countryRepository.save(newCountry);
                    });

            City city = cityRepository.findByName(hotelDTO.getAddress().getCity())
                    .orElseGet(() -> {
                        City newCity = new City();
                        newCity.setName(hotelDTO.getAddress().getCity());
                        newCity.setCountry(country);
                        return cityRepository.save(newCity);
                    });

            Street street = streetRepository.findByName(hotelDTO.getAddress().getStreet())
                    .orElseGet(() -> {
                        Street newStreet = new Street();
                        newStreet.setName(hotelDTO.getAddress().getStreet());
                        newStreet.setCity(city);
                        return streetRepository.save(newStreet);
                    });
            
            address.setStreet(street);
            address.setHotel(hotel);
            hotel.setAddress(address);
        }

        if (hotelDTO.getContacts() != null) {
            Contact contact = new Contact();
            contact.setPhones(hotelDTO.getContacts().getPhones());
            contact.setEmails(hotelDTO.getContacts().getEmails());
            contact.setHotel(hotel);
            hotel.setContact(contact);
        }

        if (hotelDTO.getArrivalTime() != null) {
            ArrivalTime arrivalTime = new ArrivalTime();
            arrivalTime.setCheckIn(hotelDTO.getArrivalTime().getCheckIn());
            arrivalTime.setCheckOut(hotelDTO.getArrivalTime().getCheckOut());
            arrivalTime.setHotel(hotel);
            hotel.setArrivalTime(arrivalTime);
        }

        if (hotelDTO.getAmenities() != null && !hotelDTO.getAmenities().isEmpty()) {
            List<Amenity> amenities = new ArrayList<>();
            for (String amenityName : hotelDTO.getAmenities()) {
                Amenity amenity = amenityRepository.findByName(amenityName)
                        .orElseGet(() -> {
                            Amenity newAmenity = new Amenity();
                            newAmenity.setName(amenityName);
                            return amenityRepository.save(newAmenity);
                        });
                amenities.add(amenity);
            }
            hotel.setAmenities(amenities);
        }

        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Hotel created successfully: id={}, name={}", savedHotel.getId(), savedHotel.getName());
        
        return hotelMapper.toDTO(savedHotel);
    }
}