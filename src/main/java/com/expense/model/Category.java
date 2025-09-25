package com.expense.model;

import java.time.LocalDateTime;

public class Category {
    private int id;
    private String name;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Category() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    public Category(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public Category(int id, String name, String description, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    //@Override
    //public String toString() {
    //    return name; // For combo box display
    //}
}