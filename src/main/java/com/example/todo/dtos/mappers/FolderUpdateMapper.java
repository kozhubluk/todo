package com.example.todo.dtos.mappers;

import com.example.todo.dtos.FolderUpdateDto;
import com.example.todo.models.Folder;
import org.mapstruct.Mapper;

@Mapper
public interface FolderUpdateMapper {
    public FolderUpdateDto toDto(Folder folder);
    public Folder toEntity(FolderUpdateDto folderUpdateDto);
}