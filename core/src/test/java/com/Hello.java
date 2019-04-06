package com;

import org.springframework.stereotype.Component;

@Component
public class Hello {
    private String message;

    public Hello() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

