package com.example.todo.dtos.mappers;

import com.example.todo.dtos.TodoDto;
import com.example.todo.dtos.TodoUpdateDto;
import com.example.todo.models.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TodoUpdateMapper {
    @Mapping(source = "todo.folder.id", target = "folderId")
    public TodoUpdateDto toDto(Todo todo);

    public Todo toEntity(TodoUpdateDto todoDto);
}
