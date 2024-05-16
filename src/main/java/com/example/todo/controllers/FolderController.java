package com.example.todo.controllers;

import com.example.todo.dtos.FolderDto;
import com.example.todo.dtos.FolderUpdateDto;
import com.example.todo.exceptions.FolderNotFoundException;
import com.example.todo.models.User;
import com.example.todo.services.FolderService;
import com.example.todo.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<FolderDto>> getAllFolders() {
        Long userId = userService.getAuthenticaticatedUser().getId();
        return ResponseEntity.ok(folderService.getAllFolders(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderDto> getFolder(@PathVariable Long id) throws FolderNotFoundException {
        Long userId = userService.getAuthenticaticatedUser().getId();
        return ResponseEntity.ok(folderService.getFolder(id, userId));
    }

    @PostMapping
    public ResponseEntity<FolderDto> addNewFolder(@RequestBody FolderUpdateDto folderUpdateDto) {
        User user = userService.getAuthenticaticatedUser();
        return ResponseEntity.ok(folderService.addNewFolder(user, folderUpdateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFolder(@PathVariable Long id, @RequestBody FolderUpdateDto folderUpdateDto) throws FolderNotFoundException {
        try {
            Long userId = userService.getAuthenticaticatedUser().getId();
            return ResponseEntity.ok(folderService.updateFolder(id, userId, folderUpdateDto));
        } catch (FolderNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFolder(@PathVariable Long id) throws FolderNotFoundException {
        try {
            Long userId = userService.getAuthenticaticatedUser().getId();
            folderService.deleteFolder(id, userId);
            return ResponseEntity.ok("Папка удалена");
        } catch (FolderNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
