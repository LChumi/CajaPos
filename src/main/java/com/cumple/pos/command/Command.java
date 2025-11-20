package com.cumple.pos.command;

public interface Command<T> {
    T execute() throws Exception;
}