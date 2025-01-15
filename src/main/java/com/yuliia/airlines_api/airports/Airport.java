package com.yuliia.airlines_api.airports;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "airports")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String name;
    private String city;
    private String country;
    private String code;


    public Airport(String name, String city, String country, String code) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.code = code;
    }
}
