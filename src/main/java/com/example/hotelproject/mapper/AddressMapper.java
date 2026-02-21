package com.example.hotelproject.mapper;

import com.example.hotelproject.dto.AddressDTO;
import com.example.hotelproject.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    
    @Mapping(target = "street", source = "street.name")
    @Mapping(target = "city", source = "street.city.name")
    @Mapping(target = "country", source = "street.city.country.name")
    @Mapping(target = "postCode", source = "postcode")
    AddressDTO toDTO(Address address);

}
