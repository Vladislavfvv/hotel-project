package com.example.hotelproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "arrival_times", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArrivalTime {
    @Id
    private Long id;

    @Column(nullable = false)
    private String checkIn;

    @Column
    private String checkOut;

    @OneToOne
    @MapsId
    @JoinColumn(name = "hotel_id")
    @JsonBackReference
    private Hotel hotel;
}
