package com.example.todo.dtos.mappers;

import com.example.todo.dtos.SubtaskDto;
import com.example.todo.dtos.SubtaskUpdateDto;
import com.example.todo.models.Subtask;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SubtaskUpdateMapper {
    @Mapping(source = "subtask.todo.id", target = "todoId")
    public SubtaskUpdateDto toDto(Subtask subtask);
    public Subtask toEntity(SubtaskUpdateDto subtaskUpdateDto);
}
