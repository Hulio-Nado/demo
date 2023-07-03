package com.example.demo.utils.exceptions;

public class NotOurRoleException extends RuntimeException{
    public NotOurRoleException(String message) {
        super(message);
    }
}
