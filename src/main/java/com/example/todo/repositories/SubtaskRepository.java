package com.example.todo.repositories;

import com.example.todo.models.Folder;
import com.example.todo.models.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    @Query(value = "SELECT * FROM subtasks WHERE todo_id = :todoId", nativeQuery = true)
    List<Subtask> getAllSubtasksByTodo(@Param("todoId") Long todoId);
}
