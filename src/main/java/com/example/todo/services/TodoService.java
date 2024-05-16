package com.example.todo.services;

import com.example.todo.dtos.TodoDto;
import com.example.todo.dtos.TodoUpdateDto;
import com.example.todo.dtos.TodoUpdateFolderDto;
import com.example.todo.dtos.mappers.TodoUpdateMapper;
import com.example.todo.exceptions.FolderNotFoundException;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.dtos.mappers.TodoMapper;
import com.example.todo.models.Folder;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.repositories.FolderRepository;
import com.example.todo.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final FolderRepository folderRepository;
    private final TodoMapper todoMapper;
    private final TodoUpdateMapper todoUpdateMapper;

    public List<TodoDto> getAllTodos(Long userId, LocalDate startDate, LocalDate endDate, Boolean completed) {
        return todoRepository.findAllByUserId(userId, startDate, endDate, completed).stream()
                .map(todoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TodoDto> getAllTodosByFolder(User user, Long folderId) {
        return todoRepository.findAllByFolder(folderId, user.getId()).stream()
                .map(todoMapper::toDto)
                .collect(Collectors.toList());
    }


    public TodoDto getTodo(Long id, Long userId) throws TodoNotFoundException {
        Todo todo = todoRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new TodoNotFoundException(
                String.format("Задача '%s' не найдена", id)
        ));

        return todoMapper.toDto(todo);
    }

    public TodoDto addNewTodo(User user, TodoUpdateDto todoUpdateDto) throws FolderNotFoundException {
        Todo todo = todoUpdateMapper.toEntity(todoUpdateDto);
        todo.setUser(user);
        Long folderId = todoUpdateDto.getFolderId();
        if (folderId != null) {
            todo.setFolder(null);
            if (folderId != -1) {
                Folder folder = folderRepository.findByIdAndUserId(todoUpdateDto.getFolderId(), user.getId()).orElseThrow(() -> new FolderNotFoundException(
                        String.format("Папка '%s' не найдена", folderId)
                ));
                todo.setFolder(folder);
            }
        }
        return todoMapper.toDto(todoRepository.save(todo));
    }

    public TodoDto updateTodo(Long id, Long userId, TodoUpdateDto todoUpdateDto) throws TodoNotFoundException, FolderNotFoundException {
        Todo todo = todoRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new TodoNotFoundException(
                String.format("Задача '%s' не найдена", id)
        ));
        Long folderId = todoUpdateDto.getFolderId();

        if (folderId != null) {
            todo.setFolder(null);
            if (folderId != -1) {
                Folder folder = folderRepository.findByIdAndUserId(todoUpdateDto.getFolderId(), userId).orElseThrow(() -> new FolderNotFoundException(
                        String.format("Папка '%s' не найдена", folderId)
                ));
                todo.setFolder(folder);
            }
        }

        Todo updatedTodo = todoUpdateMapper.toEntity(todoUpdateDto);
        todo.setTitle(updatedTodo.getTitle() != null ? updatedTodo.getTitle() : todo.getTitle());
        todo.setNotes(updatedTodo.getNotes() != null ? updatedTodo.getNotes() : todo.getNotes());
        todo.setDeadline(updatedTodo.getDeadline() != null ? updatedTodo.getDeadline() : todo.getDeadline());
        todo.setPriority(updatedTodo.getPriority() != null ? updatedTodo.getPriority() : todo.getPriority());
        todo.setCompleted(updatedTodo.getCompleted() != null ? updatedTodo.getCompleted() : todo.getCompleted());

        return todoMapper.toDto(todoRepository.save(todo));
    }

    public void deleteTodo(Long id, Long userId) throws TodoNotFoundException {
        Todo todo = todoRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new TodoNotFoundException(
                String.format("Задача '%s' не найдена", id)
        ));
        todoRepository.deleteById(id);
    }
}
