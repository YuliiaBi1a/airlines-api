package com.yuliia.airlines_api.airports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long>{

    Optional<Airport> findByCode(String code);

    @Query(value = "SELECT a FROM Airport a WHERE " +
            "(LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL) AND " +
            "(LOWER(a.code) LIKE LOWER(CONCAT('%', :code, '%')) OR :code IS NULL)")
    List<Airport> findByNameOrCode(@Param("name") String name, @Param("code") String code);
}
