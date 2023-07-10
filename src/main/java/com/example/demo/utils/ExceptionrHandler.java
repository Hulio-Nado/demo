package com.example.demo.utils;

import com.example.demo.utils.exceptions.ClientNotCreatedException;
import org.springframework.http.HttpStatus;;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionrHandler {
    @ExceptionHandler(ClientNotCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onClientNotCreatedException(ClientNotCreatedException c){
        return new ErrorResponse(c.getMessage(), LocalDateTime.now());
    }

}
