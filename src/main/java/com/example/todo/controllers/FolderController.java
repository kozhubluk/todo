package com.example.todo.controllers;

import com.example.todo.dtos.FolderDto;
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

    @PostMapping
    public ResponseEntity<FolderDto> addNewFolder(@RequestBody FolderDto folderDto) {
        User user = userService.getAuthenticaticatedUser();
        return ResponseEntity.ok(folderService.addNewFolder(user, folderDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFolder(@PathVariable Long id, @RequestBody FolderDto folderDto) throws FolderNotFoundException {
        try {
            return ResponseEntity.ok(folderService.updateFolder(id, folderDto));
        } catch (FolderNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFolder(@PathVariable Long id) throws FolderNotFoundException {
        try {
            folderService.deleteFolder(id);
            return ResponseEntity.ok("Папка удалена");
        } catch (FolderNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
