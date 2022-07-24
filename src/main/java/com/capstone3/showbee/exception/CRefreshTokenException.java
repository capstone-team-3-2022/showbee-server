package com.capstone3.showbee.exception;

public class CRefreshTokenException extends RuntimeException{
    public CRefreshTokenException() {
        super();
    }

    public CRefreshTokenException(String message) {
        super(message);
    }

    public CRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
