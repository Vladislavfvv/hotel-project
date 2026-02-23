package com.example.hotelproject.service;

import com.example.hotelproject.dto.AddressDTO;
import com.example.hotelproject.dto.ArrivalTimeDTO;
import com.example.hotelproject.dto.ContactDTO;
import com.example.hotelproject.dto.HotelDTO;
import com.example.hotelproject.dto.HotelShortDTO;
import com.example.hotelproject.entity.*;
import com.example.hotelproject.exception.HotelAlreadyExistsException;
import com.example.hotelproject.exception.HotelNotFoundException;
import com.example.hotelproject.exception.MissingSearchParameterException;
import com.example.hotelproject.mapper.HotelMapper;
import com.example.hotelproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        // Фильтруем пустые строки из списков
        brands = filterEmptyStrings(brands);
        cities = filterEmptyStrings(cities);
        countries = filterEmptyStrings(countries);
        amenities = filterEmptyStrings(amenities);

        // Проверяем, что хотя бы один параметр передан
        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasBrands = brands != null && !brands.isEmpty();
        boolean hasCities = cities != null && !cities.isEmpty();
        boolean hasCountries = countries != null && !countries.isEmpty();
        boolean hasAmenities = amenities != null && !amenities.isEmpty();

        if (!hasName && !hasBrands && !hasCities && !hasCountries && !hasAmenities) {
            throw new MissingSearchParameterException("At least one search parameter is required");
        }

        // Ищем по тому параметру, который передан
        List<Hotel> hotels;
        if (hasName) {
            hotels = hotelRepository.findByNameContainingIgnoreCase(name);
        } else if (hasBrands) {
            hotels = searchByBrands(brands);
        } else if (hasCities) {
            hotels = searchByCities(cities);
        } else if (hasCountries) {
            hotels = searchByCountries(countries);
        } else {
            hotels = searchByAmenities(amenities);
        }

        return hotelMapper.toShortDTOList(hotels);
    }

    private List<Hotel> searchByBrands(List<String> brands) {
        if (brands.size() == 1) {
            return hotelRepository.findByBrand_Name(brands.getFirst());
        }
        List<String> upperBrands = brands.stream().map(String::toUpperCase).toList();
        return hotelRepository.findByBrandNames(upperBrands);
    }

    private List<Hotel> searchByCities(List<String> cities) {
        if (cities.size() == 1) {
            return hotelRepository.findByCity(cities.getFirst());
        }
        List<String> upperCities = cities.stream().map(String::toUpperCase).toList();
        return hotelRepository.findByCities(upperCities);
    }

    private List<Hotel> searchByCountries(List<String> countries) {
        if (countries.size() == 1) {
            return hotelRepository.findByCountry(countries.getFirst());
        }
        List<String> upperCountries = countries.stream().map(String::toUpperCase).toList();
        return hotelRepository.findByCountries(upperCountries);
    }

    private List<Hotel> searchByAmenities(List<String> amenities) {
        if (amenities.size() == 1) {
            return hotelRepository.findByAmenities_Name(amenities.getFirst());
        }
        List<String> upperAmenities = amenities.stream().map(String::toUpperCase).toList();
        return hotelRepository.findByAnyAmenities(upperAmenities);
    }

    // Вспомогательный метод для фильтрации пустых строк
    private List<String> filterEmptyStrings(List<String> list) {
        if (list == null) {
            return null;
        }
        List<String> filtered = list.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .toList();
        return filtered.isEmpty() ? null : filtered;
    }

    // POST /hotels - создание нового отеля
    @Transactional
    public HotelShortDTO createHotel(HotelDTO hotelDTO) {
        log.info("Creating hotel: name={}, brand={}", hotelDTO.getName(), hotelDTO.getBrand());

        validateHotelNotExists(hotelDTO.getName());

        Hotel hotel = hotelMapper.toEntity(hotelDTO);
        hotel.setBrand(findOrCreateBrand(hotelDTO.getBrand()));

        if (hotelDTO.getAddress() != null) {
            hotel.setAddress(createAddress(hotelDTO.getAddress(), hotel));
        }

        if (hotelDTO.getContacts() != null) {
            hotel.setContact(createContact(hotelDTO.getContacts(), hotel));
        }

        if (hotelDTO.getArrivalTime() != null) {
            hotel.setArrivalTime(createArrivalTime(hotelDTO.getArrivalTime(), hotel));
        }

        if (hotelDTO.getAmenities() != null && !hotelDTO.getAmenities().isEmpty()) {
            hotel.setAmenities(findOrCreateAmenities(hotelDTO.getAmenities()));
        }

        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Hotel created successfully: id={}, name={}", savedHotel.getId(), savedHotel.getName());

        return hotelMapper.toShortDTO(savedHotel);
    }

    private void validateHotelNotExists(String name) {
        if (hotelRepository.findByNameIs(name).isPresent()) {
            log.warn("Hotel creation failed: name '{}' already exists", name);
            throw new HotelAlreadyExistsException("Hotel '" + name + "' already exists");
        }
    }

    private Brand findOrCreateBrand(String brandName) {
        return brandRepository.findByName(brandName)
                .orElseGet(() -> {
                    Brand newBrand = new Brand();
                    newBrand.setName(brandName);
                    return brandRepository.save(newBrand);
                });
    }

    private Address createAddress(AddressDTO addressDTO, Hotel hotel) {
        Address address = new Address();
        address.setHouseNumber(addressDTO.getHouseNumber());
        address.setPostcode(addressDTO.getPostCode());

        Country country = findOrCreateCountry(addressDTO.getCountry());
        City city = findOrCreateCity(addressDTO.getCity(), country);
        Street street = findOrCreateStreet(addressDTO.getStreet(), city);

        address.setStreet(street);
        address.setHotel(hotel);
        return address;
    }

    private Country findOrCreateCountry(String countryName) {
        return countryRepository.findByName(countryName)
                .orElseGet(() -> {
                    Country newCountry = new Country();
                    newCountry.setName(countryName);
                    return countryRepository.save(newCountry);
                });
    }

    private City findOrCreateCity(String cityName, Country country) {
        return cityRepository.findByName(cityName)
                .orElseGet(() -> {
                    City newCity = new City();
                    newCity.setName(cityName);
                    newCity.setCountry(country);
                    return cityRepository.save(newCity);
                });
    }

    private Street findOrCreateStreet(String streetName, City city) {
        return streetRepository.findByName(streetName)
                .orElseGet(() -> {
                    Street newStreet = new Street();
                    newStreet.setName(streetName);
                    newStreet.setCity(city);
                    return streetRepository.save(newStreet);
                });
    }

    private Contact createContact(ContactDTO contactDTO, Hotel hotel) {
        Contact contact = new Contact();
        contact.setPhone(contactDTO.getPhone());
        contact.setEmail(contactDTO.getEmail());
        contact.setHotel(hotel);
        return contact;
    }

    private ArrivalTime createArrivalTime(ArrivalTimeDTO arrivalTimeDTO, Hotel hotel) {
        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn(arrivalTimeDTO.getCheckIn());
        arrivalTime.setCheckOut(arrivalTimeDTO.getCheckOut());
        arrivalTime.setHotel(hotel);
        return arrivalTime;
    }

    private List<Amenity> findOrCreateAmenities(List<String> amenityNames) {
        List<Amenity> amenities = new ArrayList<>();
        for (String amenityName : amenityNames) {
            Amenity amenity = amenityRepository.findByName(amenityName)
                    .orElseGet(() -> {
                        Amenity newAmenity = new Amenity();
                        newAmenity.setName(amenityName);
                        return amenityRepository.save(newAmenity);
                    });
            amenities.add(amenity);
        }
        return amenities;
    }

    //POST /hotels/{id}/amenities - добавление списка amenities к отелю
    @Transactional
    public HotelDTO addAmenities(Long id, List<String> amenities) {
        log.info("Adding amenities to hotel id={}: amenities={}", id, amenities);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + id));

        for (String amenityName : amenities) {
            Amenity amenity = amenityRepository.findByName(amenityName)
                    .orElseGet(() -> {
                        Amenity newAmenity = new Amenity();
                        newAmenity.setName(amenityName);
                        return amenityRepository.save(newAmenity);
                    });

            if (!hotel.getAmenities().contains(amenity)) {
                hotel.getAmenities().add(amenity);
                log.info("Added amenity '{}' to hotel '{}'", amenityName, hotel.getName());
            } else {
                log.info("Amenity '{}' already exists for hotel '{}'", amenityName, hotel.getName());
            }
        }

        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Updated hotel: id={}, name={}, amenities count={}",
                savedHotel.getId(), savedHotel.getName(), savedHotel.getAmenities().size());

        return hotelMapper.toDTO(savedHotel);
    }

    //GET /histogram/{param} - получение колличества отелей сгруппированных по каждому значению указанного параметра. Параметр: brand, city, country, amenities.
    public Map<String, Long> getHotelListGroupByParam(String param) {
        log.info("Get group list from service with param = {}", param);

        List<Object[]> results = switch (param.toLowerCase()) {
            case "city", "cities" -> cityRepository.groupHotelsByCities();
            case "country", "countries" -> countryRepository.groupHotelsByCountry();
            case "brand", "brands" -> brandRepository.groupHotelsByBrands();
            case "amenity", "amenities" -> amenityRepository.groupHotelsByAmenities();
            default -> throw new IllegalArgumentException("Invalid parameter: " + param);
        };

        // Преобразуем List<Object[]> в Map<String, Long>
        //JPQL не может напрямую вернуть Map, поэтому возвращаем массив объектов и преобразуем в Map в Java.
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],      // название (city/country/brand/amenity)
                        row -> (Long) row[1]       // количество отелей
                ));
    }
}