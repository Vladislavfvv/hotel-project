package com.example.hotelproject.controller;

import com.example.hotelproject.dto.ErrorResponseDTO;
import com.example.hotelproject.dto.HotelDTO;
import com.example.hotelproject.dto.HotelShortDTO;
import com.example.hotelproject.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Tag(name = "Hotel Management", description = "API for managing hotels")
public class HotelController {
    private final HotelService hotelService;

    @Operation(
            summary = "Get all hotels",
            description = "Returns a list of all hotels with short information (id, name, description, address, phone)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Hotels list retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HotelShortDTO.class))
                    )
            )
    })
    @GetMapping("/hotels")
    public List<HotelShortDTO> getAllHotels() {
        return hotelService.getAllHotelsShortInfo();
    }

    @Operation(
            summary = "Get hotel by ID",
            description = "Returns full information about a specific hotel including amenities, contacts, and arrival times"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Hotel found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HotelDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping("/hotels/{id}")
    public HotelDTO getHotel(
            @Parameter(description = "Hotel ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        return hotelService.getHotelById(id);
    }

    @Operation(
            summary = "Search hotels",
            description = "Search hotels by name, brand, city, country, or amenity. Multiple values can be provided for brand, city, country, and amenity parameters. At least one parameter is required."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Hotels list retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HotelShortDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "At least one search parameter is required",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/search")
    public List<HotelShortDTO> searchHotels(
            @Parameter(description = "Hotel name (partial match)")
            @RequestParam(required = false) String name,
            @Parameter(description = "Brand name(s)")
            @RequestParam(required = false) List<String> brand,
            @Parameter(description = "City name(s)")
            @RequestParam(required = false) List<String> city,
            @Parameter(description = "Country name(s)")
            @RequestParam(required = false) List<String> country,
            @Parameter(description = "Amenity name(s)")
            @RequestParam(required = false) List<String> amenity
    ) {
        log.info("Search hotels with params: name={}, brand={}, city={}, country={}, amenity={}",
                name, brand, city, country, amenity);
        return hotelService.searchHotels(name, brand, city, country, amenity);
    }

    @Operation(
            summary = "Create new hotel",
            description = "Creates a new hotel with the provided information. Description and arrivalTime are optional."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Hotel successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HotelShortDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid hotel data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Hotel with this name already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PostMapping("/hotels")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelShortDTO createHotel(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Hotel data to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = HotelDTO.class))
            )
            @Valid @RequestBody HotelDTO hotelDTO
    ) {
        log.info("Create hotel with data: {}", hotelDTO);
        return hotelService.createHotel(hotelDTO);
    }

    @Operation(
            summary = "Add amenities to hotel",
            description = "Adds a list of amenities to an existing hotel. If amenity doesn't exist, it will be created."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Amenities successfully added",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HotelDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PostMapping("/hotels/{id}/amenities")
    public HotelDTO addAmenities(
            @Parameter(description = "Hotel ID", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of amenity names",
                    required = true,
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = String.class, example = "Free WiFi"))
                    )
            )
            @RequestBody List<String> amenities
    ) {
        log.info("Add amenities to hotel id={}: {}", id, amenities);
        return hotelService.addAmenities(id, amenities);
    }

    @Operation(
            summary = "Get hotel histogram",
            description = "Returns the count of hotels grouped by the specified parameter (brand, city, country, or amenities)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Histogram retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameter",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHotelHistogram(
            @Parameter(
                    description = "Grouping parameter",
                    required = true,
                    example = "city",
                    schema = @Schema(allowableValues = {"brand", "city", "country", "amenities"})
            )
            @PathVariable String param
    ) {
        log.info("Get grouping list from controller with params: " + param);
        return hotelService.getHotelListGroupByParam(param);
    }

}
