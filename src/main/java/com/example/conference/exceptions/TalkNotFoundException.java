package com.example.conference.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TalkNotFoundException extends RuntimeException {
    public TalkNotFoundException(String msg) {
        super(msg);
    }
}