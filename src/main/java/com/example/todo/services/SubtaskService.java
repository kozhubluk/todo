package com.example.todo.services;

import com.example.todo.dtos.SubtaskDto;
import com.example.todo.dtos.SubtaskUpdateDto;
import com.example.todo.dtos.TodoDto;
import com.example.todo.dtos.TodoUpdateDto;
import com.example.todo.dtos.mappers.SubtaskMapper;
import com.example.todo.dtos.mappers.SubtaskUpdateMapper;
import com.example.todo.exceptions.SubtaskNotFound;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.models.Folder;
import com.example.todo.models.Subtask;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.repositories.SubtaskRepository;
import com.example.todo.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final TodoRepository todoRepository;
    private final SubtaskMapper subtaskMapper;
    private final SubtaskUpdateMapper subtaskUpdateMapper;

    public List<SubtaskDto> getAllSubtasksByTodo(Long todoId)  {
        return subtaskRepository.getAllSubtasksByTodo(todoId).stream()
                .map(subtaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public SubtaskDto addNewSubtask(Long todoId, SubtaskUpdateDto subtaskUpdateDto) throws TodoNotFoundException {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException(
                String.format("Задача '%s' не найдена", todoId)
        ));
        Subtask subtask = subtaskUpdateMapper.toEntity(subtaskUpdateDto);
        subtask.setTodo(todo);
        return subtaskMapper.toDto(subtaskRepository.save(subtask));
    }

    public SubtaskDto updateSubtask(Long subtaskId, SubtaskUpdateDto subtaskUpdateDto) throws SubtaskNotFound {
        Subtask subtask = subtaskRepository.findById(subtaskId).orElseThrow(() -> new SubtaskNotFound(
                String.format("Подзадача '%s' не найдена", subtaskId)
        ));
        Subtask updatedSubtask = subtaskUpdateMapper.toEntity(subtaskUpdateDto);
        subtask.setTitle(updatedSubtask.getTitle() != null ? updatedSubtask.getTitle() : subtask.getTitle());
        subtask.setCompleted(updatedSubtask.getCompleted() != null ? updatedSubtask.getCompleted() : subtask.getCompleted());

        return subtaskMapper.toDto(subtaskRepository.save(subtask));
    }

    public void deleteSubtask(Long id) throws SubtaskNotFound {
        Subtask subtask = subtaskRepository.findById(id).orElseThrow(() -> new SubtaskNotFound(
                String.format("Подзадача '%s' не найдена", id)
        ));
        subtaskRepository.deleteById(id);
    }
}
