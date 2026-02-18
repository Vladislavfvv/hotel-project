package com.example.hotelproject.mapper;

import com.example.hotelproject.dto.AddressDTO;
import com.example.hotelproject.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    
    @Mapping(target = "street", source = "streetName")
    @Mapping(target = "city", source = "cityName")
    @Mapping(target = "country", source = "countryName")
    AddressDTO toDTO(Address address);

    @Mapping(target = "streetName", source = "street")
    @Mapping(target = "cityName", source = "city")
    @Mapping(target = "countryName", source = "country")
    Address toEntity(AddressDTO dto);
}
