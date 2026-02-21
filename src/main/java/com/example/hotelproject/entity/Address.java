package com.example.hotelproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses", indexes = {
    @Index(name = "idx_address_street", columnList = "street_id"),
    @Index(name = "idx_address_postcode", columnList = "postCode")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @Id
    private Long id;

    @Column(name = "houseNumber", nullable = false)
    private int houseNumber;

    @ManyToOne
    @JoinColumn(name = "street_id", nullable = false)
    private Street street;

    @Column(name = "postcode", nullable = false)
    private String postcode;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private Hotel hotel;

    @Transient
    public String getStreetName() {
        return street != null ? street.getName() : null;
    }
    
    @Transient
    public String getCityName() {
        return street != null && street.getCity() != null ? street.getCity().getName() : null;
    }
    
    @Transient
    public String getCountryName() {
        return street != null && street.getCity() != null && street.getCity().getCountry() != null 
            ? street.getCity().getCountry().getName() : null;
    }
}
