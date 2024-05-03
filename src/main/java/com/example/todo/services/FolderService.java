package com.example.todo.services;

import com.example.todo.dtos.FolderDto;
import com.example.todo.dtos.mappers.FolderMapper;
import com.example.todo.exceptions.FolderNotFoundException;
import com.example.todo.models.Folder;
import com.example.todo.models.User;
import com.example.todo.repositories.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;

    public List<FolderDto> getAllFolders(Long userId) {
        List<Folder> folders = folderRepository.findAllByUserId(userId);
        List<FolderDto> folderDtos = new ArrayList<>();
        for (Folder folder : folders) {
            folderDtos.add(folderMapper.toDto(folder));
        }
        return folderDtos;
    }

    public FolderDto addNewFolder(User user, FolderDto folderDto) {
        Folder folder = folderMapper.toEntity(folderDto);
        folder.setUser(user);
        return folderMapper.toDto(folderRepository.save(folder));
    }

    public FolderDto updateFolder(Long id, FolderDto folderDto) throws FolderNotFoundException {
        Folder folder = folderRepository.findById(id).orElseThrow(() -> new FolderNotFoundException(
                String.format("Папка '%s' не найдена", id)
        ));
        Folder updatedFolder = folderMapper.toEntity(folderDto);
        folder.setTitle(updatedFolder.getTitle());
        return folderMapper.toDto(folderRepository.save(folder));
    }

    public void deleteFolder(Long id) throws FolderNotFoundException {
        Folder folder = folderRepository.findById(id).orElseThrow(() -> new FolderNotFoundException(
                String.format("Папка '%s' не найдена", id)
        ));
        folderRepository.deleteById(id);
    }
}
