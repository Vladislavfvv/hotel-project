package com.example.hotelproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contacts", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {
    @Id
    private Long id;

    @ElementCollection
    @CollectionTable(name = "contact_phones", joinColumns = @JoinColumn(name = "contact_id"))
    @Column(name = "phone", nullable = false)
    @Builder.Default
    private List<String> phones = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "contact_emails", joinColumns = @JoinColumn(name = "contact_id"))
    @Column(name = "email", nullable = false)
    @Builder.Default
    private List<String> emails = new ArrayList<>();

    @OneToOne
    @MapsId
    @JoinColumn(name = "hotel_id")
    @JsonBackReference
    private Hotel hotel;
}
