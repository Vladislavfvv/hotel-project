package com.example.hotelproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "hotels", indexes = {
    @Index(name = "idx_hotel_name", columnList = "name"),
    @Index(name = "idx_hotel_brand", columnList = "brand_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Address address;

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Contact contact;

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonManagedReference
    private ArrivalTime arrivalTime;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "hotel_amenities",
        joinColumns = @JoinColumn(name = "hotel_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    @Builder.Default
    private List<Amenity> amenities = new ArrayList<>();
}
