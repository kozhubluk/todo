package com.example.todo.dtos.mappers;

import com.example.todo.dtos.SubtaskDto;
import com.example.todo.dtos.TodoDto;
import com.example.todo.models.Subtask;
import com.example.todo.models.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SubtaskMapper {
    @Mapping(source = "subtask.todo.id", target = "todoId")
    public SubtaskDto toDto(Subtask subtask);
    public Subtask toEntity(SubtaskDto subtaskDto);
}
