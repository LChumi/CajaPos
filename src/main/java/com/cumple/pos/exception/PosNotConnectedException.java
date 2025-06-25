package com.cumple.pos.exception;

public class PosNotConnectedException extends RuntimeException{
    public PosNotConnectedException(String msg) {
        super(msg);
    }
    public PosNotConnectedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
