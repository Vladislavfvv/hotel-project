package com.example.hotelproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Min(value = 1, message = "House number must be positive")
    private int houseNumber;

    @NotBlank(message = "Street is required")
    @Size(min = 2, max = 100, message = "Street must be between 2 and 100 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё\\s\\-'.]+$", message = "City must contain only letters, spaces, hyphens, apostrophes, and dots")
    private String city;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё\\s\\-'.]+$", message = "Country must contain only letters, spaces, hyphens, apostrophes, and dots")
    private String country;

    @NotBlank(message = "Post code is required")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]{3,10}$", message = "Post code must be 3-10 characters (letters, digits, spaces, hyphens allowed)")
    private String postCode;
}
