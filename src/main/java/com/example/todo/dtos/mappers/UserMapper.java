package com.example.todo.dtos.mappers;

import com.example.todo.dtos.UserDto;
import com.example.todo.models.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface UserMapper {
    public UserDto toDto (User user);
}
