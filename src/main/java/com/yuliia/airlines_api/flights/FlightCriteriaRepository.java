package com.yuliia.airlines_api.flights;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class FlightCriteriaRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Flight> findFlights(String departureAirportCode, String departureAirportName,
                                    String arrivalAirportCode, String arrivalAirportName,
                                    LocalDate departureDate, int requiredSeats) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Flight> query = cb.createQuery(Flight.class);
        Root<Flight> flight = query.from(Flight.class);

        Predicate predicate = cb.conjunction();

        if (departureAirportCode != null) {
            predicate = cb.and(predicate,
                    cb.equal(flight.get("departureAirport").get("code"), departureAirportCode));
        }
        if (departureAirportName != null) {
            predicate = cb.and(predicate,
                    cb.like(cb.lower(flight.get("departureAirport").get("name")), "%" + departureAirportName.toLowerCase() + "%"));
        }
        if (arrivalAirportCode != null) {
            predicate = cb.and(predicate,
                    cb.equal(flight.get("arrivalAirport").get("code"), arrivalAirportCode));
        }
        if (arrivalAirportName != null) {
            predicate = cb.and(predicate,
                    cb.like(cb.lower(flight.get("arrivalAirport").get("name")), "%" + arrivalAirportName.toLowerCase() + "%"));
        }
        if (departureDate != null) {
            LocalDateTime startOfDay = departureDate.atStartOfDay();
            LocalDateTime endOfDay = departureDate.atTime(LocalTime.MAX);

            predicate = cb.and(predicate,
                    cb.between(flight.get("departureTime"), startOfDay, endOfDay));
        }
        if (requiredSeats > 0) {
            predicate = cb.and(predicate, cb.ge(flight.get("availableSeats"), requiredSeats));
        }

        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }
}

