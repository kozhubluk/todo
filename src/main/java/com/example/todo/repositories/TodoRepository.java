package com.example.todo.repositories;

import com.example.todo.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query(value = "SELECT * FROM todos WHERE user_id = :userId", nativeQuery = true)
    List<Todo> findAllByUserId(@Param("userId") Long userId);
}
