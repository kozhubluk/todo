package com.example.todo.controllers;

import com.example.todo.dtos.SubtaskDto;
import com.example.todo.dtos.SubtaskUpdateDto;
import com.example.todo.exceptions.SubtaskNotFound;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.services.SubtaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class SubtaskController {
    private final SubtaskService subtaskService;

    @GetMapping("/{todoId}/subtasks")
    public ResponseEntity<?> getAllSubtasksByTodo(@PathVariable Long todoId) {
        return ResponseEntity.ok(subtaskService.getAllSubtasksByTodo(todoId));
    }

    @PostMapping("/{todoId}/subtasks")
    public ResponseEntity<?> addNewSubtask(@PathVariable Long todoId, @RequestBody SubtaskUpdateDto subtaskUpdateDto) {
        try {
            return ResponseEntity.ok(subtaskService.addNewSubtask(todoId, subtaskUpdateDto));
        } catch (TodoNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/subtasks/{id}")
    public ResponseEntity<?> updateSubtask(@PathVariable Long id, @RequestBody SubtaskUpdateDto subtaskUpdateDto) {
        try {
            return ResponseEntity.ok(subtaskService.updateSubtask(id, subtaskUpdateDto));
        } catch (SubtaskNotFound e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/subtasks/{id}")
    public ResponseEntity<String> deleteSubtask(@PathVariable Long id) {
        try {
            subtaskService.deleteSubtask(id);
            return ResponseEntity.ok("Подзадача удалена");
        } catch (SubtaskNotFound e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
