package com.test.trade;

import com.test.trade.exception.ExpiredTradeException;
import com.test.trade.exception.StaleTradeException;
import com.test.trade.exception.UnknownBookException;
import com.test.trade.exception.UnknownCounterPartyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {ExpiredTradeException.class, UnknownCounterPartyException.class,
            UnknownBookException.class, StaleTradeException.class})
    public ResponseEntity<String> handleException(Exception ex, WebRequest request){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DateTimeParseException.class})
    public ResponseEntity<String> handleDateFormatException(RuntimeException ex, WebRequest request){
        String message="Expected date format is ISO date format 'YYYY-MM-DD'";
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
