package com.yuliia.airlines_api.reservation.exceptions;

import com.yuliia.airlines_api.global.exceptions.AppException;

public class ReservationAlreadyCancelledException extends AppException {
    public ReservationAlreadyCancelledException(String message) {
        super(message);
    }
}
