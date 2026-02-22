package com.example.hotelproject.mapper;

import com.example.hotelproject.dto.HotelDTO;
import com.example.hotelproject.dto.HotelShortDTO;
import com.example.hotelproject.entity.Amenity;
import com.example.hotelproject.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface HotelMapper {

    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "amenities", source = "amenities")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "contacts", source = "contact")
    @Mapping(target = "arrivalTime", source = "arrivalTime")
    HotelDTO toDTO(Hotel hotel);
    
    List<HotelDTO> toDTOList(List<Hotel> hotels);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "arrivalTime", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel toEntity(HotelDTO hotelDTO);
    
    // Краткая информация об отеле
    @Mapping(target = "description", source = ".", qualifiedByName = "truncateDescription")
    @Mapping(target = "address", source = ".", qualifiedByName = "formatAddress")
    @Mapping(target = "phone", source = ".", qualifiedByName = "getFirstPhone")
    HotelShortDTO toShortDTO(Hotel hotel);
    
    List<HotelShortDTO> toShortDTOList(List<Hotel> hotels);
    
    // Обрезание description до 151 символа с многоточием
    @Named("truncateDescription")
    default String truncateDescription(Hotel hotel) {
        if (hotel.getDescription() == null) {
            return null;
        }
        String description = hotel.getDescription();
        if (description.length() <= 151) {
            return description;
        }
        return description.substring(0, 151) + "...";
    }
    
    // Форматирование адреса в строку
    @Named("formatAddress")
    default String formatAddress(Hotel hotel) {
        if (hotel.getAddress() == null) {
            return null;
        }
        return String.format("%d %s, %s, %s, %s",
            hotel.getAddress().getHouseNumber(),
            hotel.getAddress().getStreet().getName(),
            hotel.getAddress().getStreet().getCity().getName(),
            hotel.getAddress().getPostcode(),
            hotel.getAddress().getStreet().getCity().getCountry().getName()
        );
    }
    
    // Получение телефона
    @Named("getFirstPhone")
    default String getFirstPhone(Hotel hotel) {
        if (hotel.getContact() != null) {
            return hotel.getContact().getPhone();
        }
        return null;
    }
    
    // Преобразует список объектов Amenity в список строк для JSON
    default List<String> mapAmenities(List<Amenity> amenities) {
        if (amenities == null) {
            return null;
        }
        return amenities.stream()
            .map(Amenity::getName)
            .toList();
    }
}
