package com.example.hotelproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "streets", indexes = {
    @Index(name = "idx_street_name", columnList = "streetName"),
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

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnore
    private City city;

    @OneToMany(mappedBy = "street")
    @JsonIgnore
    @Builder.Default //для полей с инициализацией по умолчанию рекомендуется всегда использовать, иначе можно получить NPE
    private List<Address> addresses = new ArrayList<>();//т.е. сразу инициализируем и потом удобно переопределить и добавлять
}
