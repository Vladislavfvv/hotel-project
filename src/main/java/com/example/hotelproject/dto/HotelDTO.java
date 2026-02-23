package com.example.hotelproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class HotelDTO {
    
    private Long id;
    
    @NotBlank(message = "Hotel name is required")
    @Size(min = 2, max = 200, message = "Hotel name must be between 2 and 200 characters")
    private String name;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotBlank(message = "Brand is required")
    @Size(min = 2, max = 100, message = "Brand must be between 2 and 100 characters")
    private String brand;
    
    @Valid
    private AddressDTO address;
    
    @Valid
    private ContactDTO contacts;
    
    @Valid
    private ArrivalTimeDTO arrivalTime;
    
    private List<String> amenities;
}
