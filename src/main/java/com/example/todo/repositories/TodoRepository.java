package com.example.todo.repositories;

import com.example.todo.models.Folder;
import com.example.todo.models.Todo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query(value = "SELECT * FROM todos WHERE user_id = :userId " +
            "AND (deadline >= :startDate " +
            "OR cast(:startDate as date) IS NULL) " +
            "AND (deadline <= :endDate " +
            "OR cast(:endDate as date) IS NULL) " +
            "AND (completed = :isCompleted " +
            "OR :isCompleted IS NULL) " +
            "ORDER BY deadline ASC, id DESC", nativeQuery = true)
    List<Todo> findAllByUserId(@Param("userId") Long userId,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate,
                               @Param("isCompleted") Boolean completed);

    @Query(value = "SELECT * FROM todos " +
            "WHERE folder_id = :folderId " +
            "AND user_id = :userId "+
            "ORDER BY deadline ASC, id DESC",
            nativeQuery = true)
    List<Todo> findAllByFolder(@Param("folderId") Long folderId, @Param("userId") Long userId);
    Optional<Todo> findByIdAndUserId(Long id, Long userId);
}
