package com.example.todo.services;

import com.example.todo.dtos.TodoDto;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.mappers.TodoMapper;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    public List<TodoDto> getAllTodos(Long userId) {
        List<Todo> todos = todoRepository.findAllByUserId(userId);
        List<TodoDto> todoDtos = new ArrayList<>();
        for (Todo todo : todos) {
            todoDtos.add(todoMapper.toDto(todo));
        }
        return todoDtos;
    }

    public TodoDto addNewTodo(User user, TodoDto todoDto) {
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUser(user);
        return todoMapper.toDto(todoRepository.save(todo));
    }

    public TodoDto updateTodo(Long id, TodoDto todoDto) throws TodoNotFoundException {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(
                String.format("Задача '%s' не найдена", id)
        ));
        Todo updatedTodo = todoMapper.toEntity(todoDto);
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
