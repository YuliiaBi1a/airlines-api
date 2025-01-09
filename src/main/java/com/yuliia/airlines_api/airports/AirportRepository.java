package com.yuliia.airlines_api.airports;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long>{

    Optional<Airport> findByCode(String code);
}
