package com.example.todo.mappers;

import com.example.todo.dtos.UserDto;
import com.example.todo.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toUserDto (User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getName());
    }
}
