package com.example.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubtaskUpdateDto {
    private String title;
    private Boolean completed;
    private Long todoId;
}
