package com.example.todo.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortField {
    ID("id"),
    DEADLINE("deadline"),
    PRIORITY("priority");

    private final String databaseFieldName;
}
