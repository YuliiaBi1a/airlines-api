package com.yuliia.airlines_api.global.exceptions;

import com.yuliia.airlines_api.reservation.exceptions.LateCancellationException;
import com.yuliia.airlines_api.reservation.exceptions.LockExpirationException;
import com.yuliia.airlines_api.reservation.exceptions.NotEnoughSeatsException;
import com.yuliia.airlines_api.reservation.exceptions.ReservationAlreadyCancelledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(NoIdFoundException.class)
    public ResponseEntity<String> handleNoFoundException(NoIdFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LockExpirationException.class)
    public ResponseEntity<String> handleDuplicateId(LockExpirationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LateCancellationException.class)
    public ResponseEntity<String> handleLateCancellation(LateCancellationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ReservationAlreadyCancelledException.class)
    public ResponseEntity<String> handleAlreadyCancelled(ReservationAlreadyCancelledException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NotEnoughSeatsException.class)
    public ResponseEntity<String> handleNotEnoughSeats(NotEnoughSeatsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>handleNotValidException(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        for(FieldError error: exception.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

}
