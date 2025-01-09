package com.yuliia.airlines_api.airports;

public record AirportDtoResponse(  Long id,
                                   String name,
                                   String city,
                                   String country,
                                   String code) {

    public static AirportDtoResponse fromEntity(Airport airport){
        return new AirportDtoResponse(
                airport.getId(),
                airport.getName(),
                airport.getCity(),
                airport.getCountry(),
                airport.getCode()
        );
    }
}
