package com.example.todo.controllers;

import com.example.todo.dtos.TodoDto;
import com.example.todo.dtos.TodoUpdateDto;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.models.User;
import com.example.todo.services.TodoService;
import com.example.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;
    private final UserService userService;

    @GetMapping("/todos")
    public ResponseEntity<List<TodoDto>> getAllTodos(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate startDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate endDate,
                                                     @RequestParam(required = false) Boolean completed) {
        Long userId = userService.getAuthenticaticatedUser().getId();
        return ResponseEntity.ok(todoService.getAllTodos(userId, startDate, endDate, completed));
    }

    @GetMapping("/{folderId}/todos")
    public ResponseEntity<List<TodoDto>> getAllTodosByFolder(@PathVariable Long folderId) {
        User user = userService.getAuthenticaticatedUser();
        return ResponseEntity.ok(todoService.getAllTodosByFolder(user, folderId));
    }

    @PostMapping("/todos")
    public ResponseEntity<TodoDto> addNewTodo(@RequestBody TodoUpdateDto todoUpdateDto) {
        User user = userService.getAuthenticaticatedUser();
        return ResponseEntity.ok(todoService.addNewTodo(user, todoUpdateDto));
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody TodoUpdateDto todoUpdateDto) throws TodoNotFoundException {
        try {
            return ResponseEntity.ok(todoService.updateTodo(id, todoUpdateDto));
        } catch ( TodoNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/to/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) throws TodoNotFoundException {
        try {
            todoService.deleteTodo(id);
            return ResponseEntity.ok("Задача удалена");
        } catch (TodoNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
