package com.example.todo.exceptions;

public class FolderNotFoundException extends Exception{
    public FolderNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
