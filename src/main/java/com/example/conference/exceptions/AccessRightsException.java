package com.example.conference.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccessRightsException extends RuntimeException {
    public AccessRightsException(String msg) {
        super(msg);
    }
}