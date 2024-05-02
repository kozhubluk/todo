package com.example.todo.exceptions;

public class TodoNotFoundException extends Exception{
    public TodoNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
