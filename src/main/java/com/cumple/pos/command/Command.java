package com.cumple.pos.command;

public interface Command<T> {
    T exceute() throws Exception;
}
