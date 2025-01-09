package com.yuliia.airlines_api.airports;

public record AirportDtoRequest(
        String name,
        String city,
        String country,
        String code
) {
    public Airport toEntity() {
        return new Airport(
                this.name,
                this.city,
                this.country,
                this.code
        );
    }
}
