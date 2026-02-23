package com.example.hotelproject.service;

import com.example.hotelproject.dto.HotelDTO;
import com.example.hotelproject.dto.HotelShortDTO;
import com.example.hotelproject.entity.Address;
import com.example.hotelproject.entity.Amenity;
import com.example.hotelproject.entity.Brand;
import com.example.hotelproject.entity.City;
import com.example.hotelproject.entity.Contact;
import com.example.hotelproject.entity.Country;
import com.example.hotelproject.entity.Hotel;
import com.example.hotelproject.entity.Street;
import com.example.hotelproject.exception.HotelNotFoundException;
import com.example.hotelproject.mapper.HotelMapper;
import com.example.hotelproject.repository.AmenityRepository;
import com.example.hotelproject.repository.BrandRepository;
import com.example.hotelproject.repository.CityRepository;
import com.example.hotelproject.repository.CountryRepository;
import com.example.hotelproject.repository.HotelRepository;
import com.example.hotelproject.repository.StreetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("HotelService Unit Tests")
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private StreetRepository streetRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel1;
    private Hotel hotel2;
    private HotelShortDTO hotelShortDTO1;
    private HotelShortDTO hotelShortDTO2;
    private HotelDTO hotelDTO;

    @BeforeEach
    void setUp() {
        Brand brand = Brand.builder().id(1L).name("Hilton").build();
        Country country = Country.builder().id(1L).name("Belarus").build();
        City city = City.builder().id(1L).name("Minsk").country(country).build();
        Street street = Street.builder().id(1L).name("Pobediteley Avenue").city(city).build();
        
        Address address1 = Address.builder()
                .id(1L)
                .houseNumber(9)
                .street(street)
                .postcode("220004")
                .build();
        
        Contact contact1 = Contact.builder()
                .id(1L)
                .phone("+375 17 309-80-00")
                .email("info@doubletree.com")
                .build();
        
        hotel1 = Hotel.builder()
                .id(1L)
                .name("DoubleTree by Hilton Minsk")
                .description("Luxury hotel in Minsk")
                .brand(brand)
                .address(address1)
                .contact(contact1)
                .build();
        
        hotel2 = Hotel.builder()
                .id(2L)
                .name("Hampton by Hilton Minsk")
                .description("Comfortable hotel")
                .brand(brand)
                .build();
        
        hotelShortDTO1 = HotelShortDTO.builder()
                .id(1L)
                .name("DoubleTree by Hilton Minsk")
                .description("Luxury hotel in Minsk")
                .address("9 Pobediteley Avenue, Minsk, 220004, Belarus")
                .phone("+375 17 309-80-00")
                .build();
        
        hotelShortDTO2 = HotelShortDTO.builder()
                .id(2L)
                .name("Hampton by Hilton Minsk")
                .description("Comfortable hotel")
                .build();
        
        hotelDTO = HotelDTO.builder()
                .id(1L)
                .name("DoubleTree by Hilton Minsk")
                .description("Luxury hotel in Minsk")
                .brand("Hilton")
                .build();
    }

    @Test
    @DisplayName("getAllHotelsShortInfo - should return list of hotels when hotels exist")
    void getAllHotelsShortInfo_ShouldReturnListOfHotels_WhenHotelsExist() {
        // Given
        List<Hotel> hotels = Arrays.asList(hotel1, hotel2);
        List<HotelShortDTO> expectedDTOs = Arrays.asList(hotelShortDTO1, hotelShortDTO2);
        
        when(hotelRepository.findAll()).thenReturn(hotels);
        when(hotelMapper.toShortDTOList(hotels)).thenReturn(expectedDTOs);
        
        // When
        List<HotelShortDTO> result = hotelService.getAllHotelsShortInfo();
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(hotelShortDTO1, hotelShortDTO2);
        
        verify(hotelRepository, times(1)).findAll();
        verify(hotelMapper, times(1)).toShortDTOList(hotels);
    }

    @Test
    @DisplayName("getAllHotelsShortInfo - should return empty list when no hotels exist")
    void getAllHotelsShortInfo_ShouldReturnEmptyList_WhenNoHotelsExist() {
        // Given
        List<Hotel> emptyList = Collections.emptyList();
        
        when(hotelRepository.findAll()).thenReturn(emptyList);
        when(hotelMapper.toShortDTOList(emptyList)).thenReturn(Collections.emptyList());
        
        // When
        List<HotelShortDTO> result = hotelService.getAllHotelsShortInfo();
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        
        verify(hotelRepository, times(1)).findAll();
        verify(hotelMapper, times(1)).toShortDTOList(emptyList);
    }

    @Test
    @DisplayName("getHotelById - should return hotel when hotel exists")
    void getHotelById_ShouldReturnHotel_WhenHotelExists() {
        // Given
        Long hotelId = 1L;
        
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel1));
        when(hotelMapper.toDTO(hotel1)).thenReturn(hotelDTO);
        
        // When
        HotelDTO result = hotelService.getHotelById(hotelId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("DoubleTree by Hilton Minsk");
        assertThat(result.getBrand()).isEqualTo("Hilton");
        
        verify(hotelRepository, times(1)).findById(hotelId);
        verify(hotelMapper, times(1)).toDTO(hotel1);
    }

    @Test
    @DisplayName("getHotelById - should throw HotelNotFoundException when hotel does not exist")
    void getHotelById_ShouldThrowException_WhenHotelDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        
        when(hotelRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> hotelService.getHotelById(nonExistentId))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessage("Hotel not found with id: " + nonExistentId);
        
        verify(hotelRepository, times(1)).findById(nonExistentId);
        verify(hotelMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("getHotelById - should handle null id")
    void getHotelById_ShouldHandleNullId() {
        // Given
        Long nullId = null;
        
        when(hotelRepository.findById(nullId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> hotelService.getHotelById(nullId))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessage("Hotel not found with id: null");
        
        verify(hotelRepository, times(1)).findById(nullId);
    }

    @Test
    @DisplayName("addAmenities - should add new amenities to hotel when hotel exists")
    void addAmenities_ShouldAddNewAmenitiesToHotel_WhenHotelExists() {
        // Given
        Long hotelId = 1L;
        List<String> amenityNames = Arrays.asList("Free WiFi", "Parking");
        
        Amenity amenity1 = Amenity.builder().id(1L).name("Free WiFi").build();
        Amenity amenity2 = Amenity.builder().id(2L).name("Parking").build();
        
        hotel1.setAmenities(new ArrayList<>());
        
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel1));
        when(amenityRepository.findByName("Free WiFi")).thenReturn(Optional.of(amenity1));
        when(amenityRepository.findByName("Parking")).thenReturn(Optional.of(amenity2));
        when(hotelRepository.save(hotel1)).thenReturn(hotel1);
        when(hotelMapper.toDTO(hotel1)).thenReturn(hotelDTO);
        
        // When
        HotelDTO result = hotelService.addAmenities(hotelId, amenityNames);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(hotel1.getAmenities()).hasSize(2);
        assertThat(hotel1.getAmenities()).contains(amenity1, amenity2);
        
        verify(hotelRepository, times(1)).findById(hotelId);
        verify(amenityRepository, times(1)).findByName("Free WiFi");
        verify(amenityRepository, times(1)).findByName("Parking");
        verify(hotelRepository, times(1)).save(hotel1);
        verify(hotelMapper, times(1)).toDTO(hotel1);
    }

    @Test
    @DisplayName("addAmenities - should create new amenity when amenity does not exist")
    void addAmenities_ShouldCreateNewAmenity_WhenAmenityDoesNotExist() {
        // Given
        Long hotelId = 1L;
        List<String> amenityNames = Arrays.asList("New Amenity");
        
        Amenity newAmenity = Amenity.builder().id(3L).name("New Amenity").build();
        
        hotel1.setAmenities(new ArrayList<>());
        
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel1));
        when(amenityRepository.findByName("New Amenity")).thenReturn(Optional.empty());
        when(amenityRepository.save(any(Amenity.class))).thenReturn(newAmenity);
        when(hotelRepository.save(hotel1)).thenReturn(hotel1);
        when(hotelMapper.toDTO(hotel1)).thenReturn(hotelDTO);
        
        // When
        HotelDTO result = hotelService.addAmenities(hotelId, amenityNames);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(hotel1.getAmenities()).hasSize(1);
        assertThat(hotel1.getAmenities()).contains(newAmenity);
        
        verify(hotelRepository, times(1)).findById(hotelId);
        verify(amenityRepository, times(1)).findByName("New Amenity");
        verify(amenityRepository, times(1)).save(any(Amenity.class));
        verify(hotelRepository, times(1)).save(hotel1);
    }

    @Test
    @DisplayName("addAmenities - should not add duplicate amenities")
    void addAmenities_ShouldNotAddDuplicateAmenities() {
        // Given
        Long hotelId = 1L;
        List<String> amenityNames = Arrays.asList("Free WiFi");
        
        Amenity existingAmenity = Amenity.builder().id(1L).name("Free WiFi").build();
        
        hotel1.setAmenities(new ArrayList<>());
        hotel1.getAmenities().add(existingAmenity);
        
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel1));
        when(amenityRepository.findByName("Free WiFi")).thenReturn(Optional.of(existingAmenity));
        when(hotelRepository.save(hotel1)).thenReturn(hotel1);
        when(hotelMapper.toDTO(hotel1)).thenReturn(hotelDTO);
        
        // When
        HotelDTO result = hotelService.addAmenities(hotelId, amenityNames);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(hotel1.getAmenities()).hasSize(1);
        
        verify(hotelRepository, times(1)).findById(hotelId);
        verify(amenityRepository, times(1)).findByName("Free WiFi");
        verify(hotelRepository, times(1)).save(hotel1);
    }

    @Test
    @DisplayName("addAmenities - should throw HotelNotFoundException when hotel does not exist")
    void addAmenities_ShouldThrowException_WhenHotelDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        List<String> amenityNames = Arrays.asList("Free WiFi");
        
        when(hotelRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> hotelService.addAmenities(nonExistentId, amenityNames))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessage("Hotel not found with id: " + nonExistentId);
        
        verify(hotelRepository, times(1)).findById(nonExistentId);
        verify(amenityRepository, never()).findByName(anyString());
        verify(hotelRepository, never()).save(any());
    }

    @Test
    @DisplayName("addAmenities - should handle empty amenities list")
    void addAmenities_ShouldHandleEmptyAmenitiesList() {
        // Given
        Long hotelId = 1L;
        List<String> emptyAmenities = Collections.emptyList();
        
        hotel1.setAmenities(new ArrayList<>());
        
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel1));
        when(hotelRepository.save(hotel1)).thenReturn(hotel1);
        when(hotelMapper.toDTO(hotel1)).thenReturn(hotelDTO);
        
        // When
        HotelDTO result = hotelService.addAmenities(hotelId, emptyAmenities);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(hotel1.getAmenities()).isEmpty();
        
        verify(hotelRepository, times(1)).findById(hotelId);
        verify(amenityRepository, never()).findByName(anyString());
        verify(hotelRepository, times(1)).save(hotel1);
    }

    @Test
    @DisplayName("createHotel - should create hotel successfully with all data")
    void createHotel_ShouldCreateHotelSuccessfully_WithAllData() {
        // Given
        HotelDTO newHotelDTO = HotelDTO.builder()
                .name("New Test Hotel")
                .brand("Hilton")
                .description("Test description")
                .build();
        
        Brand brand = Brand.builder().id(1L).name("Hilton").build();
        Hotel newHotel = Hotel.builder()
                .name("New Test Hotel")
                .brand(brand)
                .description("Test description")
                .build();
        
        Hotel savedHotel = Hotel.builder()
                .id(10L)
                .name("New Test Hotel")
                .brand(brand)
                .description("Test description")
                .build();
        
        when(hotelRepository.findByNameIs("New Test Hotel")).thenReturn(Optional.empty());
        when(hotelMapper.toEntity(newHotelDTO)).thenReturn(newHotel);
        when(brandRepository.findByName("Hilton")).thenReturn(Optional.of(brand));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(savedHotel);
        when(hotelMapper.toShortDTO(savedHotel)).thenReturn(hotelShortDTO1);
        
        // When
        HotelShortDTO result = hotelService.createHotel(newHotelDTO);
        
        // Then
        assertThat(result).isNotNull();
        
        verify(hotelRepository, times(1)).findByNameIs("New Test Hotel");
        verify(brandRepository, times(1)).findByName("Hilton");
        verify(hotelRepository, times(1)).save(any(Hotel.class));
        verify(hotelMapper, times(1)).toShortDTO(savedHotel);
    }

    @Test
    @DisplayName("createHotel - should throw exception when hotel already exists")
    void createHotel_ShouldThrowException_WhenHotelAlreadyExists() {
        // Given
        HotelDTO newHotelDTO = HotelDTO.builder()
                .name("DoubleTree by Hilton Minsk")
                .brand("Hilton")
                .build();
        
        when(hotelRepository.findByNameIs("DoubleTree by Hilton Minsk")).thenReturn(Optional.of(hotel1));
        
        // When & Then
        assertThatThrownBy(() -> hotelService.createHotel(newHotelDTO))
                .isInstanceOf(com.example.hotelproject.exception.HotelAlreadyExistsException.class)
                .hasMessageContaining("Hotel 'DoubleTree by Hilton Minsk' already exists");
        
        verify(hotelRepository, times(1)).findByNameIs("DoubleTree by Hilton Minsk");
        verify(hotelRepository, never()).save(any());
    }

    @Test
    @DisplayName("searchHotels - should search by name")
    void searchHotels_ShouldSearchByName() {
        // Given
        String name = "Hilton";
        List<Hotel> hotels = Arrays.asList(hotel1, hotel2);
        List<HotelShortDTO> expectedDTOs = Arrays.asList(hotelShortDTO1, hotelShortDTO2);
        
        when(hotelRepository.findByNameContainingIgnoreCase(name)).thenReturn(hotels);
        when(hotelMapper.toShortDTOList(hotels)).thenReturn(expectedDTOs);
        
        // When
        List<HotelShortDTO> result = hotelService.searchHotels(name, null, null, null, null);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(hotelShortDTO1, hotelShortDTO2);
        
        verify(hotelRepository, times(1)).findByNameContainingIgnoreCase(name);
        verify(hotelMapper, times(1)).toShortDTOList(hotels);
    }

    @Test
    @DisplayName("searchHotels - should search by single brand")
    void searchHotels_ShouldSearchBySingleBrand() {
        // Given
        List<String> brands = Arrays.asList("Hilton");
        List<Hotel> hotels = Arrays.asList(hotel1, hotel2);
        List<HotelShortDTO> expectedDTOs = Arrays.asList(hotelShortDTO1, hotelShortDTO2);
        
        when(hotelRepository.findByBrand_Name("Hilton")).thenReturn(hotels);
        when(hotelMapper.toShortDTOList(hotels)).thenReturn(expectedDTOs);
        
        // When
        List<HotelShortDTO> result = hotelService.searchHotels(null, brands, null, null, null);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        verify(hotelRepository, times(1)).findByBrand_Name("Hilton");
        verify(hotelMapper, times(1)).toShortDTOList(hotels);
    }

    @Test
    @DisplayName("searchHotels - should search by multiple brands")
    void searchHotels_ShouldSearchByMultipleBrands() {
        // Given
        List<String> brands = Arrays.asList("Hilton", "Marriott");
        List<Hotel> hotels = Arrays.asList(hotel1, hotel2);
        List<HotelShortDTO> expectedDTOs = Arrays.asList(hotelShortDTO1, hotelShortDTO2);
        
        when(hotelRepository.findByBrandNames(Arrays.asList("HILTON", "MARRIOTT"))).thenReturn(hotels);
        when(hotelMapper.toShortDTOList(hotels)).thenReturn(expectedDTOs);
        
        // When
        List<HotelShortDTO> result = hotelService.searchHotels(null, brands, null, null, null);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        verify(hotelRepository, times(1)).findByBrandNames(Arrays.asList("HILTON", "MARRIOTT"));
        verify(hotelMapper, times(1)).toShortDTOList(hotels);
    }

    @Test
    @DisplayName("searchHotels - should throw exception when no parameters provided")
    void searchHotels_ShouldThrowException_WhenNoParametersProvided() {
        // When & Then
        assertThatThrownBy(() -> hotelService.searchHotels(null, null, null, null, null))
                .isInstanceOf(com.example.hotelproject.exception.MissingSearchParameterException.class)
                .hasMessage("At least one search parameter is required");
        
        verify(hotelRepository, never()).findByNameContainingIgnoreCase(anyString());
    }

    @Test
    @DisplayName("searchHotels - should return empty list when no hotels found")
    void searchHotels_ShouldReturnEmptyList_WhenNoHotelsFound() {
        // Given
        String name = "NonExistent";
        List<Hotel> emptyList = Collections.emptyList();
        
        when(hotelRepository.findByNameContainingIgnoreCase(name)).thenReturn(emptyList);
        when(hotelMapper.toShortDTOList(emptyList)).thenReturn(Collections.emptyList());
        
        // When
        List<HotelShortDTO> result = hotelService.searchHotels(name, null, null, null, null);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        
        verify(hotelRepository, times(1)).findByNameContainingIgnoreCase(name);
    }

    @Test
    @DisplayName("getHotelListGroupByParam - should group by city")
    void getHotelListGroupByParam_ShouldGroupByCity() {
        // Given
        List<Object[]> results = Arrays.asList(
                new Object[]{"Minsk", 3L},
                new Object[]{"Warsaw", 2L}
        );
        
        when(cityRepository.groupHotelsByCities()).thenReturn(results);
        
        // When
        Map<String, Long> result = hotelService.getHotelListGroupByParam("city");
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get("Minsk")).isEqualTo(3L);
        assertThat(result.get("Warsaw")).isEqualTo(2L);
        
        verify(cityRepository, times(1)).groupHotelsByCities();
    }

    @Test
    @DisplayName("getHotelListGroupByParam - should group by brand")
    void getHotelListGroupByParam_ShouldGroupByBrand() {
        // Given
        List<Object[]> results = Arrays.asList(
                new Object[]{"Hilton", 5L},
                new Object[]{"Marriott", 3L}
        );
        
        when(brandRepository.groupHotelsByBrands()).thenReturn(results);
        
        // When
        Map<String, Long> result = hotelService.getHotelListGroupByParam("brand");
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get("Hilton")).isEqualTo(5L);
        assertThat(result.get("Marriott")).isEqualTo(3L);
        
        verify(brandRepository, times(1)).groupHotelsByBrands();
    }

    @Test
    @DisplayName("getHotelListGroupByParam - should throw exception for invalid parameter")
    void getHotelListGroupByParam_ShouldThrowException_ForInvalidParameter() {
        // When & Then
        assertThatThrownBy(() -> hotelService.getHotelListGroupByParam("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid parameter: invalid");
    }
}
