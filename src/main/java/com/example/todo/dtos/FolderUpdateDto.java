package com.example.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FolderUpdateDto {
    Long id;
    String title;
}
