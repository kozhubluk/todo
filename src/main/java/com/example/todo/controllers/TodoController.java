package com.example.todo.controllers;

import com.example.todo.dtos.TodoDto;
import com.example.todo.dtos.TodoUpdateDto;
import com.example.todo.exceptions.FolderNotFoundException;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.models.User;
import com.example.todo.services.TodoService;
import com.example.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;
    private final UserService userService;

    @GetMapping("/todos")
    public ResponseEntity<List<TodoDto>> getAllTodos(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                     @RequestParam(required = false) Boolean completed) {
        Long userId = userService.getAuthenticaticatedUser().getId();
        return ResponseEntity.ok(todoService.getAllTodos(userId, startDate, endDate, completed));
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getTodo(@PathVariable Long id) {
        try {
            Long userId = userService.getAuthenticaticatedUser().getId();
            return ResponseEntity.ok(todoService.getTodo(id, userId));
        } catch (TodoNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{folderId}/todos")
    public ResponseEntity<?> getAllTodosByFolder(@PathVariable Long folderId) {
        try {
            User user = userService.getAuthenticaticatedUser();
            return ResponseEntity.ok(todoService.getAllTodosByFolder(user, folderId));
        } catch (MethodArgumentTypeMismatchException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/todos")
    public ResponseEntity<?> addNewTodo(@RequestBody TodoUpdateDto todoUpdateDto) {
        try {
            User user = userService.getAuthenticaticatedUser();
            return ResponseEntity.ok(todoService.addNewTodo(user, todoUpdateDto));
        } catch (FolderNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody TodoUpdateDto todoUpdateDto) {
        try {
            Long userId = userService.getAuthenticaticatedUser().getId();
            return ResponseEntity.ok(todoService.updateTodo(id, userId, todoUpdateDto));
        } catch (TodoNotFoundException | FolderNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) throws TodoNotFoundException {
        try {
            Long userId = userService.getAuthenticaticatedUser().getId();
            todoService.deleteTodo(id, userId);
            return new ResponseEntity<String>("Задача успешно удалена", HttpStatus.OK);
        } catch (TodoNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
