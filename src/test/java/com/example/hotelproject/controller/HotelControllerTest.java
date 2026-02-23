package com.example.hotelproject.controller;

import com.example.hotelproject.dto.HotelDTO;
import com.example.hotelproject.dto.HotelShortDTO;
import com.example.hotelproject.exception.HotelNotFoundException;
import com.example.hotelproject.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
@DisplayName("HotelController Unit Tests")
@SuppressWarnings("deprecation")
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HotelService hotelService;

    private HotelShortDTO hotelShortDTO1;
    private HotelShortDTO hotelShortDTO2;
    private HotelDTO hotelDTO;

    @BeforeEach
    void setUp() {
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
                .address("8 Tolstogo Street, Minsk, 220007, Belarus")
                .phone("+375 17 215-40-00")
                .build();

        hotelDTO = HotelDTO.builder()
                .id(1L)
                .name("DoubleTree by Hilton Minsk")
                .description("Luxury hotel in Minsk")
                .brand("Hilton")
                .build();
    }

    @Test
    @DisplayName("GET /property-view/hotels - should return list of hotels with status 200")
    void getAllHotels_ShouldReturnListOfHotels_WithStatus200() throws Exception {
        // Given
        List<HotelShortDTO> hotels = Arrays.asList(hotelShortDTO1, hotelShortDTO2);
        when(hotelService.getAllHotelsShortInfo()).thenReturn(hotels);

        // When & Then
        mockMvc.perform(get("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("DoubleTree by Hilton Minsk")))
                .andExpect(jsonPath("$[0].description", is("Luxury hotel in Minsk")))
                .andExpect(jsonPath("$[0].address", is("9 Pobediteley Avenue, Minsk, 220004, Belarus")))
                .andExpect(jsonPath("$[0].phone", is("+375 17 309-80-00")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Hampton by Hilton Minsk")));

        verify(hotelService, times(1)).getAllHotelsShortInfo();
    }

    @Test
    @DisplayName("GET /property-view/hotels - should return empty list when no hotels exist")
    void getAllHotels_ShouldReturnEmptyList_WhenNoHotelsExist() throws Exception {
        // Given
        when(hotelService.getAllHotelsShortInfo()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(hotelService, times(1)).getAllHotelsShortInfo();
    }

    @Test
    @DisplayName("GET /property-view/hotels/{id} - should return hotel with status 200 when hotel exists")
    void getHotel_ShouldReturnHotel_WithStatus200_WhenHotelExists() throws Exception {
        // Given
        Long hotelId = 1L;
        when(hotelService.getHotelById(hotelId)).thenReturn(hotelDTO);

        // When & Then
        mockMvc.perform(get("/property-view/hotels/{id}", hotelId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("DoubleTree by Hilton Minsk")))
                .andExpect(jsonPath("$.description", is("Luxury hotel in Minsk")))
                .andExpect(jsonPath("$.brand", is("Hilton")));

        verify(hotelService, times(1)).getHotelById(hotelId);
    }

    @Test
    @DisplayName("GET /property-view/hotels/{id} - should return 404 when hotel does not exist")
    void getHotel_ShouldReturn404_WhenHotelDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;
        when(hotelService.getHotelById(nonExistentId))
                .thenThrow(new HotelNotFoundException("Hotel not found with id: " + nonExistentId));

        // When & Then
        mockMvc.perform(get("/property-view/hotels/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Hotel not found with id: " + nonExistentId)));

        verify(hotelService, times(1)).getHotelById(nonExistentId);
    }

    @Test
    @DisplayName("GET /property-view/hotels/{id} - should handle invalid id format")
    void getHotel_ShouldReturn404_WhenIdFormatIsInvalid() throws Exception {
        // When & Then
        mockMvc.perform(get("/property-view/hotels/{id}", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(hotelService, never()).getHotelById(anyLong());
    }

    @Test
    @DisplayName("GET /property-view/hotels/0 - should handle zero id")
    void getHotel_ShouldHandleZeroId() throws Exception {
        // Given
        Long zeroId = 0L;
        when(hotelService.getHotelById(zeroId))
                .thenThrow(new HotelNotFoundException("Hotel not found with id: " + zeroId));

        // When & Then
        mockMvc.perform(get("/property-view/hotels/{id}", zeroId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Hotel not found with id: 0")));

        verify(hotelService, times(1)).getHotelById(zeroId);
    }

    @Test
    @DisplayName("GET /property-view/hotels/{id} - should handle negative id")
    void getHotel_ShouldHandleNegativeId() throws Exception {
        // Given
        Long negativeId = -1L;
        when(hotelService.getHotelById(negativeId))
                .thenThrow(new HotelNotFoundException("Hotel not found with id: " + negativeId));

        // When & Then
        mockMvc.perform(get("/property-view/hotels/{id}", negativeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Hotel not found with id: -1")));

        verify(hotelService, times(1)).getHotelById(negativeId);
    }

    @Test
    @DisplayName("POST /property-view/hotels/{id}/amenities - should add amenities with status 200")
    void addAmenities_ShouldAddAmenities_WithStatus200() throws Exception {
        // Given
        Long hotelId = 1L;
        List<String> amenities = Arrays.asList("Free WiFi", "Parking");
        
        HotelDTO updatedHotel = HotelDTO.builder()
                .id(1L)
                .name("DoubleTree by Hilton Minsk")
                .brand("Hilton")
                .amenities(amenities)
                .build();
        
        when(hotelService.addAmenities(hotelId, amenities)).thenReturn(updatedHotel);

        // When & Then
        mockMvc.perform(post("/property-view/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("DoubleTree by Hilton Minsk")))
                .andExpect(jsonPath("$.brand", is("Hilton")))
                .andExpect(jsonPath("$.amenities", hasSize(2)))
                .andExpect(jsonPath("$.amenities[0]", is("Free WiFi")))
                .andExpect(jsonPath("$.amenities[1]", is("Parking")));

        verify(hotelService, times(1)).addAmenities(hotelId, amenities);
    }

    @Test
    @DisplayName("POST /property-view/hotels/{id}/amenities - should return 404 when hotel not found")
    void addAmenities_ShouldReturn404_WhenHotelNotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;
        List<String> amenities = Arrays.asList("Free WiFi");
        
        when(hotelService.addAmenities(nonExistentId, amenities))
                .thenThrow(new HotelNotFoundException("Hotel not found with id: " + nonExistentId));

        // When & Then
        mockMvc.perform(post("/property-view/hotels/{id}/amenities", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Hotel not found with id: 999")));

        verify(hotelService, times(1)).addAmenities(nonExistentId, amenities);
    }

    @Test
    @DisplayName("POST /property-view/hotels/{id}/amenities - should handle empty amenities list")
    void addAmenities_ShouldHandleEmptyList() throws Exception {
        // Given
        Long hotelId = 1L;
        List<String> emptyAmenities = Collections.emptyList();
        
        when(hotelService.addAmenities(hotelId, emptyAmenities)).thenReturn(hotelDTO);

        // When & Then
        mockMvc.perform(post("/property-view/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyAmenities)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(hotelService, times(1)).addAmenities(hotelId, emptyAmenities);
    }

    @Test
    @DisplayName("POST /property-view/hotels/{id}/amenities - should handle single amenity")
    void addAmenities_ShouldHandleSingleAmenity() throws Exception {
        // Given
        Long hotelId = 1L;
        List<String> singleAmenity = Arrays.asList("Pool");
        
        HotelDTO updatedHotel = HotelDTO.builder()
                .id(1L)
                .name("DoubleTree by Hilton Minsk")
                .brand("Hilton")
                .amenities(singleAmenity)
                .build();
        
        when(hotelService.addAmenities(hotelId, singleAmenity)).thenReturn(updatedHotel);

        // When & Then
        mockMvc.perform(post("/property-view/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(singleAmenity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities", hasSize(1)))
                .andExpect(jsonPath("$.amenities[0]", is("Pool")));

        verify(hotelService, times(1)).addAmenities(hotelId, singleAmenity);
    }

    @Test
    @DisplayName("POST /property-view/hotels - should create hotel with status 201")
    void createHotel_ShouldCreateHotel_WithStatus201() throws Exception {
        // Given
        HotelDTO newHotelDTO = HotelDTO.builder()
                .name("New Test Hotel")
                .brand("Hilton")
                .description("Test description")
                .build();
        
        HotelShortDTO createdHotel = HotelShortDTO.builder()
                .id(10L)
                .name("New Test Hotel")
                .description("Test description")
                .build();
        
        when(hotelService.createHotel(any(HotelDTO.class))).thenReturn(createdHotel);

        // When & Then
        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHotelDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("New Test Hotel")))
                .andExpect(jsonPath("$.description", is("Test description")));

        verify(hotelService, times(1)).createHotel(any(HotelDTO.class));
    }

    @Test
    @DisplayName("POST /property-view/hotels - should return 409 when hotel already exists")
    void createHotel_ShouldReturn409_WhenHotelAlreadyExists() throws Exception {
        // Given
        HotelDTO newHotelDTO = HotelDTO.builder()
                .name("DoubleTree by Hilton Minsk")
                .brand("Hilton")
                .build();
        
        when(hotelService.createHotel(any(HotelDTO.class)))
                .thenThrow(new com.example.hotelproject.exception.HotelAlreadyExistsException(
                        "Hotel with name 'DoubleTree by Hilton Minsk' already exists"));

        // When & Then
        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHotelDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", is("Hotel with name 'DoubleTree by Hilton Minsk' already exists")));

        verify(hotelService, times(1)).createHotel(any(HotelDTO.class));
    }

    @Test
    @DisplayName("POST /property-view/hotels - should return 400 for invalid data")
    void createHotel_ShouldReturn400_ForInvalidData() throws Exception {
        // Given - hotel without required name field
        HotelDTO invalidHotelDTO = HotelDTO.builder()
                .brand("Hilton")
                .build();

        // When & Then
        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidHotelDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")));

        verify(hotelService, never()).createHotel(any(HotelDTO.class));
    }

    @Test
    @DisplayName("GET /property-view/search - should search hotels by name")
    void searchHotels_ShouldSearchByName_WithStatus200() throws Exception {
        // Given
        String name = "Hilton";
        List<HotelShortDTO> hotels = Arrays.asList(hotelShortDTO1, hotelShortDTO2);
        
        when(hotelService.searchHotels(name, null, null, null, null)).thenReturn(hotels);

        // When & Then
        mockMvc.perform(get("/property-view/search")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("DoubleTree by Hilton Minsk")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Hampton by Hilton Minsk")));

        verify(hotelService, times(1)).searchHotels(name, null, null, null, null);
    }

    @Test
    @DisplayName("GET /property-view/search - should search hotels by brand")
    void searchHotels_ShouldSearchByBrand_WithStatus200() throws Exception {
        // Given
        List<String> brands = Arrays.asList("Hilton");
        List<HotelShortDTO> hotels = Arrays.asList(hotelShortDTO1, hotelShortDTO2);
        
        when(hotelService.searchHotels(null, brands, null, null, null)).thenReturn(hotels);

        // When & Then
        mockMvc.perform(get("/property-view/search")
                        .param("brand", "Hilton")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(hotelService, times(1)).searchHotels(null, brands, null, null, null);
    }

    @Test
    @DisplayName("GET /property-view/search - should search hotels by multiple brands")
    void searchHotels_ShouldSearchByMultipleBrands_WithStatus200() throws Exception {
        // Given
        List<String> brands = Arrays.asList("Hilton", "Marriott");
        List<HotelShortDTO> hotels = Arrays.asList(hotelShortDTO1, hotelShortDTO2);
        
        when(hotelService.searchHotels(null, brands, null, null, null)).thenReturn(hotels);

        // When & Then
        mockMvc.perform(get("/property-view/search")
                        .param("brand", "Hilton", "Marriott")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(hotelService, times(1)).searchHotels(null, brands, null, null, null);
    }

    @Test
    @DisplayName("GET /property-view/search - should return 422 when no parameters provided")
    void searchHotels_ShouldReturn422_WhenNoParametersProvided() throws Exception {
        // Given
        when(hotelService.searchHotels(null, null, null, null, null))
                .thenThrow(new com.example.hotelproject.exception.MissingSearchParameterException(
                        "At least one search parameter is required"));

        // When & Then
        mockMvc.perform(get("/property-view/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(422)))
                .andExpect(jsonPath("$.error", is("Unprocessable Entity")))
                .andExpect(jsonPath("$.message", is("At least one search parameter is required")));

        verify(hotelService, times(1)).searchHotels(null, null, null, null, null);
    }

    @Test
    @DisplayName("GET /property-view/search - should return empty list when no hotels found")
    void searchHotels_ShouldReturnEmptyList_WhenNoHotelsFound() throws Exception {
        // Given
        String name = "NonExistent";
        
        when(hotelService.searchHotels(name, null, null, null, null)).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/property-view/search")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(hotelService, times(1)).searchHotels(name, null, null, null, null);
    }

    @Test
    @DisplayName("GET /property-view/histogram/{param} - should return histogram by city")
    void getHotelHistogram_ShouldReturnHistogramByCity_WithStatus200() throws Exception {
        // Given
        Map<String, Long> histogram = new java.util.HashMap<>();
        histogram.put("Minsk", 3L);
        histogram.put("Warsaw", 2L);
        
        when(hotelService.getHotelListGroupByParam("city")).thenReturn(histogram);

        // When & Then
        mockMvc.perform(get("/property-view/histogram/city")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Minsk", is(3)))
                .andExpect(jsonPath("$.Warsaw", is(2)));

        verify(hotelService, times(1)).getHotelListGroupByParam("city");
    }

    @Test
    @DisplayName("GET /property-view/histogram/{param} - should return histogram by brand")
    void getHotelHistogram_ShouldReturnHistogramByBrand_WithStatus200() throws Exception {
        // Given
        Map<String, Long> histogram = new java.util.HashMap<>();
        histogram.put("Hilton", 5L);
        histogram.put("Marriott", 3L);
        
        when(hotelService.getHotelListGroupByParam("brand")).thenReturn(histogram);

        // When & Then
        mockMvc.perform(get("/property-view/histogram/brand")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Hilton", is(5)))
                .andExpect(jsonPath("$.Marriott", is(3)));

        verify(hotelService, times(1)).getHotelListGroupByParam("brand");
    }

    @Test
    @DisplayName("GET /property-view/histogram/{param} - should return 400 for invalid parameter")
    void getHotelHistogram_ShouldReturn400_ForInvalidParameter() throws Exception {
        // Given
        when(hotelService.getHotelListGroupByParam("invalid"))
                .thenThrow(new IllegalArgumentException("Invalid parameter: invalid"));

        // When & Then
        mockMvc.perform(get("/property-view/histogram/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Invalid parameter: invalid")));

        verify(hotelService, times(1)).getHotelListGroupByParam("invalid");
    }
}
