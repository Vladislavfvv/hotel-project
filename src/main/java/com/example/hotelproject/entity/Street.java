package com.example.hotelproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "streets", schema = "public", indexes = {
    @Index(name = "idx_street_name", columnList = "name"),
    @Index(name = "idx_street_city", columnList = "city_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Street {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnore
    private City city;

    @OneToMany(mappedBy = "street")
    @JsonIgnore
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();
}
