package com.example.todo.dtos.mappers;


import com.example.todo.dtos.FolderDto;
import com.example.todo.models.Folder;
import org.mapstruct.Mapper;


@Mapper
public interface FolderMapper {
    public FolderDto toDto(Folder folder);
    public Folder toEntity(FolderDto folderDto);
}