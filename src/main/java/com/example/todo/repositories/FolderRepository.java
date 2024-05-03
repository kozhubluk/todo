package com.example.todo.repositories;

import com.example.todo.models.Folder;
import com.example.todo.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query(value = "SELECT * FROM folders WHERE user_id = :userId ORDER BY deadline", nativeQuery = true)
    List<Folder> findAllByUserId(@Param("userId") Long userId);
}
