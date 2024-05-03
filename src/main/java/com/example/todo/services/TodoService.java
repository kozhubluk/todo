package com.example.todo.services;

import com.example.todo.dtos.TodoDto;
import com.example.todo.dtos.TodoUpdateDto;
import com.example.todo.dtos.mappers.TodoUpdateMapper;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.dtos.mappers.TodoMapper;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final TodoUpdateMapper todoUpdateMapper;

    public List<TodoDto> getAllTodos(Long userId, LocalDate startDate, LocalDate endDate, Boolean completed) {
        List<Todo> todos = todoRepository.findAllByUserId(userId, startDate, endDate, completed);
        System.out.println(startDate);
        System.out.println();
        System.out.println();
        System.out.println(endDate);
        todoRepository.findAllByUserId(userId, startDate, endDate, completed);


        List<TodoDto> todoDtos = new ArrayList<>();
        for (Todo todo : todos) {
            todoDtos.add(todoMapper.toDto(todo));
        }
        return todoDtos;
    }

    public TodoDto addNewTodo(User user, TodoUpdateDto todoUpdateDto) {
        Todo todo = todoUpdateMapper.toEntity(todoUpdateDto);
        todo.setUser(user);
        return todoMapper.toDto(todoRepository.save(todo));
    }

    public TodoDto updateTodo(Long id, TodoUpdateDto todoUpdateDto) throws TodoNotFoundException {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(
                String.format("Задача '%s' не найдена", id)
        ));
        Todo updatedTodo = todoUpdateMapper.toEntity(todoUpdateDto);
        todo.setTitle(updatedTodo.getTitle());
        todo.setNotes(updatedTodo.getNotes());
        todo.setDeadline(updatedTodo.getDeadline());
        todo.setPriority(updatedTodo.getPriority());
        todo.setCompleted(updatedTodo.isCompleted());


        return todoMapper.toDto(todoRepository.save(todo));
    }

    public void deleteTodo(Long id) throws TodoNotFoundException {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(
                String.format("Задача '%s' не найдена", id)
        ));
        todoRepository.deleteById(id);
    }
}
