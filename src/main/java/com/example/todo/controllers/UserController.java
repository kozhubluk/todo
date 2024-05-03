package com.example.todo.controllers;

import com.example.todo.dtos.UserDto;
import com.example.todo.dtos.mappers.UserMapper;
import com.example.todo.models.User;
import com.example.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/secured")
    public String securedData() {
        return "Secured data";
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser() {
        User user = userService.getAuthenticaticatedUser();
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping("/user")
    public ResponseEntity<UserDto> updateCategory(@RequestBody UserDto userDto) {
        User user = userService.updateAuthenticatedUser(userDto);
        return ResponseEntity.ok().body(userMapper.toDto(user));
    }
}
