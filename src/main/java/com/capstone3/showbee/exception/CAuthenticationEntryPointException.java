package com.capstone3.showbee.exception;

public class CAuthenticationEntryPointException extends RuntimeException {
    public CAuthenticationEntryPointException(String msg, Throwable t){
        super(msg, t);
    }

    public CAuthenticationEntryPointException(String msg)
    {
        super(msg);
    }

    public CAuthenticationEntryPointException(){
        super();
    }
}
