package com.example.todo.services;

import com.example.todo.dtos.FolderDto;
import com.example.todo.dtos.FolderUpdateDto;
import com.example.todo.dtos.mappers.FolderMapper;
import com.example.todo.dtos.mappers.FolderUpdateMapper;
import com.example.todo.exceptions.FolderNotFoundException;
import com.example.todo.models.Folder;
import com.example.todo.models.User;
import com.example.todo.repositories.FolderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private FolderMapper folderMapper;

    @Mock
    private FolderUpdateMapper folderUpdateMapper;

    @InjectMocks
    private FolderService folderService;

    private User testUser;
    private Folder testFolder;
    private FolderUpdateDto testFolderUpdateDto;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testFolder = new Folder();
        testFolderUpdateDto = new FolderUpdateDto(1L, "folder");
    }

    @Test
    public void getAllFolders() {
        when(folderRepository.findAllByUserId(1L)).thenReturn(Arrays.asList(testFolder));
        when(folderMapper.toDto(testFolder)).thenReturn(new FolderDto(1L, "folder"));

        List<FolderDto> result = folderService.getAllFolders(1L);

        assertEquals(1, result.size());
        verify(folderRepository).findAllByUserId(1L);
        verify(folderMapper).toDto(testFolder);
    }

    @Test
    public void addNewFolder() {
        when(folderUpdateMapper.toEntity(testFolderUpdateDto)).thenReturn(testFolder);
        when(folderRepository.save(testFolder)).thenReturn(testFolder);
        when(folderMapper.toDto(testFolder)).thenReturn(new FolderDto(1L, "folder"));

        FolderDto result = folderService.addNewFolder(testUser, testFolderUpdateDto);

        verify(folderUpdateMapper).toEntity(testFolderUpdateDto);
        verify(folderRepository).save(testFolder);
        verify(folderMapper).toDto(testFolder);
    }

    @Test
    public void getFolderFolderNotFound() {
        when(folderRepository.findByIdAndUserId(1L, 1L)).thenReturn(java.util.Optional.empty());

        assertThrows(FolderNotFoundException.class, () -> folderService.getFolder(1L, 1L));
    }

    @Test
    void deleteFolder() throws FolderNotFoundException {
        Long folderId = 1L;
        Folder folder = new Folder();
        folder.setId(folderId);

        when(folderRepository.findByIdAndUserId(folderId, 1L)).thenReturn(Optional.of(folder));

        folderService.deleteFolder(folderId, 1L);

        verify(folderRepository, times(1)).findByIdAndUserId(folderId, 1L);
        verify(folderRepository, times(1)).deleteById(folderId);
    }

    @Test
    void deleteFolderFolderNotFound() {
        Long folderId = 1L;

        when(folderRepository.findByIdAndUserId(folderId, 1L)).thenReturn(Optional.empty());

        FolderNotFoundException exception = assertThrows(FolderNotFoundException.class,
                () -> folderService.deleteFolder(folderId, 1L));

        assertEquals(String.format("Папка '%s' не найдена", folderId), exception.getMessage());
    }

    @Test
    void updateFolder() throws FolderNotFoundException {
        // given
        Long id = 1L;
        FolderUpdateDto folderUpdateDto = new FolderUpdateDto(id, "new title");
        Folder existingFolder = new Folder();
        existingFolder.setId(id);
        existingFolder.setTitle("old title");

        Folder updatedFolder = new Folder();
        existingFolder.setId(id);
        existingFolder.setTitle("new title");

        FolderDto updatedFolderDto = new FolderDto(id, "New Folder Title");

        when(folderRepository.findByIdAndUserId(id, 1L)).thenReturn(Optional.of(existingFolder));
        when(folderUpdateMapper.toEntity(folderUpdateDto)).thenReturn(updatedFolder);
        when(folderRepository.save(existingFolder)).thenReturn(updatedFolder);
        when(folderMapper.toDto(updatedFolder)).thenReturn(updatedFolderDto);

        FolderDto result = folderService.updateFolder(id, 1L, folderUpdateDto);

        verify(folderRepository, times(1)).findByIdAndUserId(id, 1L);
        verify(folderUpdateMapper, times(1)).toEntity(folderUpdateDto);
        verify(folderRepository, times(1)).save(existingFolder);
        verify(folderMapper, times(1)).toDto(updatedFolder);
    }

    @Test
    void updateFolderFolderNotFound() {
        Long folderId = 1L;
        FolderUpdateDto folderUpdateDto = new FolderUpdateDto(1L, "folder");

        when(folderRepository.findByIdAndUserId(folderId, 1L)).thenReturn(Optional.empty());

        FolderNotFoundException exception = assertThrows(FolderNotFoundException.class,
                () -> folderService.updateFolder(folderId, 1L, folderUpdateDto));

        assertEquals(String.format("Папка '%s' не найдена", folderId), exception.getMessage());
    }
}


