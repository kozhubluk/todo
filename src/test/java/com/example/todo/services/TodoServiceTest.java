package com.example.todo.services;


import com.example.todo.dtos.TodoDto;
import com.example.todo.dtos.TodoUpdateDto;
import com.example.todo.dtos.mappers.TodoMapper;
import com.example.todo.dtos.mappers.TodoUpdateMapper;
import com.example.todo.exceptions.FolderNotFoundException;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.models.Folder;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.repositories.FolderRepository;
import com.example.todo.repositories.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private TodoMapper todoMapper;

    @Mock
    private TodoUpdateMapper todoUpdateMapper;

    @InjectMocks
    private TodoService todoService;

    @Test
    void getAllTodos() {
        // Arrange
        Long userId = 1L;
        LocalDate startDate = LocalDate.of(2023, 10, 26);
        LocalDate endDate = LocalDate.of(2023, 10, 28);
        Boolean completed = true;

        Todo todo1 = new Todo();
        todo1.setId(1L);
        todo1.setTitle("Task1");
        todo1.setDeadline(startDate);
        todo1.setCompleted(false);

        Todo todo2 = new Todo();
        todo2.setId(2L);
        todo2.setTitle("Task2");
        todo2.setDeadline(startDate);
        todo2.setCompleted(false);

        List<Todo> todos = List.of(todo1, todo2);

        TodoDto todoDto1 = new TodoDto(1L, "Task 1", null, startDate, null, false, null);
        TodoDto todoDto2 = new TodoDto(2L, "Task 2", null, startDate, null, false, null);
        List<TodoDto> expectedTodos = List.of(todoDto1, todoDto2);

        when(todoRepository.findAllByUserId(userId, startDate, endDate, completed)).thenReturn(todos);
        when(todoMapper.toDto(todo1)).thenReturn(todoDto1);
        when(todoMapper.toDto(todo2)).thenReturn(todoDto2);

        List<TodoDto> actualTodos = todoService.getAllTodos(userId, startDate, endDate, completed);

        assertEquals(expectedTodos, actualTodos);
        verify(todoRepository, times(1)).findAllByUserId(userId, startDate, endDate, completed);
        verify(todoMapper, times(1)).toDto(todo1);
        verify(todoMapper, times(1)).toDto(todo2);
    }


    @Test
    void getTodo() throws TodoNotFoundException {
        Todo todo = new Todo();
        TodoDto todoDto = new TodoDto(1L, "title", "", null, 1, false, 1L);

        when(todoRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(todo));
        when(todoMapper.toDto(todo)).thenReturn(todoDto);

        TodoDto result = todoService.getTodo(1L, 1L);

        assertEquals(todoDto, result);
        verify(todoRepository).findByIdAndUserId(1L, 1L);
        verify(todoMapper).toDto(todo);
    }

    @Test
    void getTodoTodoNotFound() {
        when(todoRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.getTodo(1L, 1L));
        verify(todoRepository).findByIdAndUserId(1L, 1L);
    }


    @Test
    void addNewTodoWithoutFolderId() throws FolderNotFoundException {
        // given
        User user = new User();
        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("title", "", null, 1, false, null);
        Todo todo = new Todo();
        TodoDto expectedDto = new TodoDto(1L, "title", "", null, 1, false, null);

        when(todoUpdateMapper.toEntity(todoUpdateDto)).thenReturn(todo);
        when(todoRepository.save(todo)).thenReturn(todo);
        when(todoMapper.toDto(todo)).thenReturn(expectedDto);

        TodoDto actualDto = todoService.addNewTodo(user, todoUpdateDto);

        verify(todoRepository).save(todo);
        verify(folderRepository, never()).findByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    void addNewTodoFolderNotFound() throws FolderNotFoundException {
        User user = new User();
        user.setId(1L);
        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("title", "", null, 1, false, 1L);
        todoUpdateDto.setFolderId(2L);
        Todo todo = new Todo();

        when(todoUpdateMapper.toEntity(todoUpdateDto)).thenReturn(todo);
        when(folderRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.empty());

        assertThrows(FolderNotFoundException.class, () -> todoService.addNewTodo(user, todoUpdateDto));
        verify(folderRepository).findByIdAndUserId(2L, 1L);
        verify(todoRepository, never()).save(todo);
    }


    @Test
    void updateTodoTodoNotFound() throws TodoNotFoundException, FolderNotFoundException {
        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("title", "", null, 1, false, 1L);

        when(todoRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.updateTodo(1L, 1L, todoUpdateDto));
        verify(todoRepository).findByIdAndUserId(1L, 1L);
        verify(todoUpdateMapper, never()).toEntity(todoUpdateDto);
        verify(todoRepository, never()).save(any(Todo.class));
    }


    @Test
    void deleteTodo() throws TodoNotFoundException {
        Todo todo = new Todo();
        todo.setId(1L);

        when(todoRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(todo));

        todoService.deleteTodo(1L, 1L);

        verify(todoRepository).findByIdAndUserId(1L, 1L);
        verify(todoRepository).deleteById(1L);
    }

    @Test
    void deleteTodoTodoNotFound() {
        when(todoRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.deleteTodo(1L, 1L));
        verify(todoRepository).findByIdAndUserId(1L, 1L);
        verify(todoRepository, never()).deleteById(anyLong());
    }
}