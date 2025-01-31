package com.yuliia.airlines_api.global.exceptions;

import com.yuliia.airlines_api.reservation.exceptions.LockExpirationException;
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
  /*
    @ExceptionHandler(NoRegistersFoundException.class)
    public ResponseEntity<String> handleNotFoundRegisters(NoRegistersFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoIdFoundException.class)
    public ResponseEntity<String> handleNotFoundId(NoIdFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFinishGameException.class)
    public ResponseEntity<String> handleNoFinishGame(NotFinishGameException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DependencyException.class)
    public ResponseEntity<String> handleDependencyExist(DependencyException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NoPlayerNameFoundException.class)
    public ResponseEntity<String> handleNotFoundName(NoPlayerNameFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoPlayingException.class)
    public ResponseEntity<String> handleNotFoundPlayingPlayers(NoPlayingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }*/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>handleNotValidException(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        for(FieldError error: exception.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }


}
