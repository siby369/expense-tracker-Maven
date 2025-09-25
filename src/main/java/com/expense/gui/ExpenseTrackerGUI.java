package com.expense.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.expense.dao.ExpenseTrackerDAO;
import com.expense.model.Category;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ExpenseTrackerGUI extends JFrame {

    private ExpenseTrackerDAO categoryDAO;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public ExpenseTrackerGUI() {
        this.categoryDAO = new ExpenseTrackerDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadCategories();
    }

    private void initializeComponents() {
        setTitle("Expense Tracker - Categories");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Name", "Description", "Created At", "Updated At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        categoryTable = new JTable(tableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        nameField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        addButton = new JButton("Add Category");
        updateButton = new JButton("Update Category");
        deleteButton = new JButton("Delete Category");
        refreshButton = new JButton("Refresh Categories");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        inputPanel.add(new JLabel("Name"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Description"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JScrollPane(descriptionArea), gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(new JScrollPane(categoryTable), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Select a category to update or delete"));
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        addButton.addActionListener(e -> addCategory());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());
        refreshButton.addActionListener(e -> loadCategories());

        // this.addWindowListener(e -> {});

        categoryTable.getSelectionModel().addListSelectionListener(
            e -> { if (!e.getValueIsAdjusting()) loadSelectedCategory(); }
        );
    }

    private void addCategory() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (name.isEmpty()) 
            return;
        try {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            categoryDAO.createCategory(category);
            loadCategories();
        } catch (SQLException ignored) {}
    }

    private void updateCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) 
            return;

        String name = nameField.getText().trim();
        if (name.isEmpty()) 
            return;

        int id = (int) categoryTable.getValueAt(row, 0);
        String description = descriptionArea.getText().trim();

        try {
            Category category = categoryDAO.getCategoryById(id);
            if (category != null) {
                category.setName(name);
                category.setDescription(description);
                categoryDAO.updateCategory(category);
                loadCategories();
            }
        } catch (SQLException ignored) {}
    }

    private void deleteCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) return;

        int id = (int) categoryTable.getValueAt(row, 0);
        try {
            categoryDAO.deleteCategory(id);
            loadCategories();
        } catch (SQLException ignored) {}
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            updateTable(categories);
        } catch (SQLException ignored) {}
    }

    private void updateTable(List<Category> categories) {
        tableModel.setRowCount(0);
        for (Category c : categories) {
            Object[] row = {
                    c.getId(),
                    c.getName(),
                    c.getDescription(),
                    c.getCreated_at(),
                    c.getUpdated_at()
            };
            tableModel.addRow(row);
        }
    }

    private void loadSelectedCategory() {
        int row = categoryTable.getSelectedRow();
        if (row != -1) {
            nameField.setText(tableModel.getValueAt(row, 1).toString());
            descriptionArea.setText(tableModel.getValueAt(row, 2).toString());
        }
    }
}
