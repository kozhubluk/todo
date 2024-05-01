package com.example.todo.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "subtasks")
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "completed")
    private boolean completed;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "todo_id")
    private Todo todo;
}
