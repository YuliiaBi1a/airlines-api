package com.yuliia.airlines_api.reservation.exceptions;

import com.yuliia.airlines_api.global.exceptions.AppException;

public class LockExpirationException extends AppException {
    public LockExpirationException(String message) {
        super(message);
    }
}
