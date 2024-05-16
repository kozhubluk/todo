package com.example.todo.repositories;

import com.example.todo.models.Folder;
import com.example.todo.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query(value = "SELECT * FROM folders " +
            "WHERE user_id = :userId " +
            "ORDER BY id DESC", nativeQuery = true)
    List<Folder> findAllByUserId(@Param("userId") Long userId);
    Optional<List<Folder>> findAllByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    Optional<Folder> findByIdAndUserId(Long id, Long userId);
}
