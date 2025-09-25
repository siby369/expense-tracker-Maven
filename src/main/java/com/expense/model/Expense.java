package com.expense.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Expense {
    private int id;
    private String title;
    private String description;
    private BigDecimal amount;
    private int categoryId;
    private String categoryName; // i may use it for display purposes i think
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Expense() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    public Expense(String title, String description, BigDecimal amount, int categoryId) {
        this();
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.categoryId = categoryId;
    }

    public Expense(int id, String title, String description, BigDecimal amount, int categoryId,String categoryName, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}