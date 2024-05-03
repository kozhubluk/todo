package com.example.todo.exceptions;

public class SubtaskNotFound extends Exception{
    public SubtaskNotFound(String errorMessage) {
        super(errorMessage);
    }
}
