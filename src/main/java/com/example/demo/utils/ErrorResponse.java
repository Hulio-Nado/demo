package com.example.demo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private String message;
    private LocalDateTime dateTime;

}
