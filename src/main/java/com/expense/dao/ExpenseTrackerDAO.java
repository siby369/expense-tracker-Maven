package com.expense.dao;

import com.expense.util.DatabaseConnection;
import com.expense.model.Category;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.sql.*;

public class ExpenseTrackerDAO {

    //cat queries
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM categories ORDER BY created_at DESC";
    private static final String INSERT_CATEGORY = "INSERT INTO categories(name, description, created_at, updated_at) VALUES(?, ?, ?, ?)";
    private static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM categories WHERE id = ?";
    private static final String UPDATE_CATEGORY = "UPDATE categories SET name = ?, description = ?, updated_at = ? WHERE id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM categories WHERE id = ?";

    //Cat

    public int createCategory(Category category) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(category.getCreated_at()));
            stmt.setTimestamp(4, Timestamp.valueOf(category.getUpdated_at()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        }
    }

    private Category getCategoryRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
        
        return new Category(id, name, description, createdAt, updatedAt);
    }
    
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();

        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CATEGORIES);
            ResultSet res = stmt.executeQuery()
        ) {
            while (res.next()) {
                categories.add(getCategoryRow(res));
            }
        }
        return categories;
    }

    public Category getCategoryById(int categoryId) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_CATEGORY_BY_ID)
        ) {
            stmt.setInt(1, categoryId);
            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    return getCategoryRow(res);
                }
            }
        }
        return null;
    }

    public boolean updateCategory(Category category) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CATEGORY)
        ) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, category.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteCategory(int categoryId) throws SQLException {
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_CATEGORY)
        ) {
            stmt.setInt(1, categoryId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}