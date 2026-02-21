package com.example.hotelproject.dto;

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
    private String name;
    private String description;
    private String brand;
    private AddressDTO address;
    private ContactDTO contacts;
    private ArrivalTimeDTO arrivalTime;
    private List<String> amenities;
}
