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
public class AddressDTO {
    private int houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}
