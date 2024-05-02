package com.example.todo.mappers;

import com.example.todo.dtos.TodoDto;

import com.example.todo.models.Todo;
import org.mapstruct.Mapper;


@Mapper
public interface TodoMapper {
    public TodoDto toDto(Todo todo);
    public Todo toEntity(TodoDto todoDto);
}
