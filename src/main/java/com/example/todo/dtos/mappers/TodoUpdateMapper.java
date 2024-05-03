package com.example.todo.dtos.mappers;

import com.example.todo.dtos.TodoDto;
import com.example.todo.dtos.TodoUpdateDto;
import com.example.todo.models.Todo;
import org.mapstruct.Mapper;

@Mapper
public interface TodoUpdateMapper {
    public TodoUpdateDto toDto(Todo todo);

    public Todo toEntity(TodoUpdateDto todoDto);
}
