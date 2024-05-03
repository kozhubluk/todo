package com.example.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FolderDto {
    Long id;
    String title;
}
