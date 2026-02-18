package com.example.hotelproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelShortDTO {
    
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
