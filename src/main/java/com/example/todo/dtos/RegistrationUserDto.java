package com.example.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RegistrationUserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String name;
}