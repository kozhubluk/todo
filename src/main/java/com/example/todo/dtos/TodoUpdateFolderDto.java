package com.example.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TodoUpdateFolderDto {
    private Long folderId;
}