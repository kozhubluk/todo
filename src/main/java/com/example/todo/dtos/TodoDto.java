package com.example.todo.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TodoDto {
    private Long id;
    private String title;
    private String notes;
    private LocalDate deadline;
    private int priority;
    private boolean completed;

}
