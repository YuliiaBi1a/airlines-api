package com.yuliia.airlines_api.reservation.exceptions;

import com.yuliia.airlines_api.global.exceptions.AppException;

public class LateCancellationException extends AppException {
    public LateCancellationException(String message) {
        super(message);
    }
}
