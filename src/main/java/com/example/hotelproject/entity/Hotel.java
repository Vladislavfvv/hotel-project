package com.example.hotelproject.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "hotels", schema = "public", indexes = {
    @Index(name = "idx_hotel_name", columnList = "name"),
    @Index(name = "idx_hotel_brand", columnList = "brand")
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
    private List<Amenity> amenities = new ArrayList<>();
}
