package com.example.hotelproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
