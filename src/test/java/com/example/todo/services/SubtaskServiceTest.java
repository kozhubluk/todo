package com.example.todo.services;


import com.example.todo.dtos.SubtaskDto;
import com.example.todo.dtos.SubtaskUpdateDto;
import com.example.todo.dtos.mappers.SubtaskMapper;
import com.example.todo.dtos.mappers.SubtaskUpdateMapper;
import com.example.todo.exceptions.SubtaskNotFound;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.models.Subtask;
import com.example.todo.models.Todo;
import com.example.todo.repositories.SubtaskRepository;
import com.example.todo.repositories.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubtaskServiceTest {

    @Mock
    private SubtaskRepository subtaskRepository;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private SubtaskMapper subtaskMapper;

    @Mock
    private SubtaskUpdateMapper subtaskUpdateMapper;

    @InjectMocks
    private SubtaskService subtaskService;

    @Test
    void getAllSubtasksByTodo() {
        Long todoId = 1L;
        List<Subtask> subtasks = List.of(
                new Subtask(),
                new Subtask()
        );
        List<SubtaskDto> expectedSubtasks = List.of(
                new SubtaskDto(1L, "title", false, todoId),
                new SubtaskDto(2L, "title", false, null)
        );
        when(subtaskRepository.getAllSubtasksByTodo(todoId)).thenReturn(subtasks);

        List<SubtaskDto> actualSubtasks = subtaskService.getAllSubtasksByTodo(todoId);

        assertEquals(expectedSubtasks.size(), actualSubtasks.size());
        verify(subtaskRepository, times(1)).getAllSubtasksByTodo(todoId);
        verify(subtaskMapper, times(2)).toDto(any(Subtask.class));
    }

    @Test
    void addNewSubtask() throws TodoNotFoundException {
        Long todoId = 1L;
        SubtaskUpdateDto subtaskUpdateDto = new SubtaskUpdateDto("title", true, 1L);
        Subtask subtask = new Subtask();
        subtask.setId(todoId);
        SubtaskDto expectedSubtaskDto = new SubtaskDto(todoId, "title", true, 1L);
        Todo todo = new Todo();
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));
        when(subtaskUpdateMapper.toEntity(subtaskUpdateDto)).thenReturn(subtask);
        when(subtaskRepository.save(subtask)).thenReturn(subtask);
        when(subtaskMapper.toDto(subtask)).thenReturn(expectedSubtaskDto);

        SubtaskDto actualSubtaskDto = subtaskService.addNewSubtask(todoId, subtaskUpdateDto);

        assertEquals(expectedSubtaskDto, actualSubtaskDto);
        verify(todoRepository, times(1)).findById(todoId);
        verify(subtaskUpdateMapper, times(1)).toEntity(subtaskUpdateDto);
        verify(subtaskRepository, times(1)).save(subtask);
        verify(subtaskMapper, times(1)).toDto(subtask);
    }

    @Test
    void addNewSubtaskTodoNotFound() {
        Long todoId = 1L;
        SubtaskUpdateDto subtaskUpdateDto = new SubtaskUpdateDto( "title", true, null);
        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> subtaskService.addNewSubtask(todoId, subtaskUpdateDto));
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    void updateSubtask() throws SubtaskNotFound {
        // Arrange
        Long subtaskId = 1L;
        Long todoId = 1L;
        SubtaskUpdateDto subtaskUpdateDto = new SubtaskUpdateDto("title", false, todoId);
        Subtask subtask = new Subtask();
        Subtask updatedSubtask = new Subtask();
        SubtaskDto expectedSubtaskDto = new SubtaskDto(subtaskId, "title", false, todoId);
        when(subtaskRepository.findById(subtaskId)).thenReturn(Optional.of(subtask));
        when(subtaskUpdateMapper.toEntity(subtaskUpdateDto)).thenReturn(updatedSubtask);
        when(subtaskRepository.save(subtask)).thenReturn(subtask);
        when(subtaskMapper.toDto(subtask)).thenReturn(expectedSubtaskDto);

        SubtaskDto actualSubtaskDto = subtaskService.updateSubtask(subtaskId, subtaskUpdateDto);

        assertEquals(expectedSubtaskDto, actualSubtaskDto);
        verify(subtaskRepository, times(1)).findById(subtaskId);
        verify(subtaskUpdateMapper, times(1)).toEntity(subtaskUpdateDto);
        verify(subtaskRepository, times(1)).save(subtask);
        verify(subtaskMapper, times(1)).toDto(subtask);
    }

    @Test
    void updateSubtaskSubtaskNotFound() {
        Long subtaskId = 1L;
        Long todoId = 1L;
        SubtaskUpdateDto subtaskUpdateDto = new SubtaskUpdateDto("title", false, todoId);
        when(subtaskRepository.findById(subtaskId)).thenReturn(Optional.empty());

        assertThrows(SubtaskNotFound.class, () -> subtaskService.updateSubtask(subtaskId, subtaskUpdateDto));
        verify(subtaskRepository, times(1)).findById(subtaskId);
    }

    @Test
    void deleteSubtask() throws SubtaskNotFound {
        Long subtaskId = 1L;
        Subtask subtask = new Subtask();
        when(subtaskRepository.findById(subtaskId)).thenReturn(Optional.of(subtask));

        subtaskService.deleteSubtask(subtaskId);

        verify(subtaskRepository, times(1)).findById(subtaskId);
        verify(subtaskRepository, times(1)).deleteById(subtaskId);
    }

    @Test
    void deleteSubtaskSubtaskNotFound() {
        Long subtaskId = 1L;
        when(subtaskRepository.findById(subtaskId)).thenReturn(Optional.empty());

        assertThrows(SubtaskNotFound.class, () -> subtaskService.deleteSubtask(subtaskId));
        verify(subtaskRepository, times(1)).findById(subtaskId);
    }
}
