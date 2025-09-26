package com.expense.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.expense.dao.ExpenseTrackerDAO;
import com.expense.model.Category;
import com.expense.model.Expense;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;

public class ExpenseTrackerGUI extends JFrame {

    private ExpenseTrackerDAO dao;

    private JTable categoryTable;
    private DefaultTableModel categoryTableModel;
    private JTextField categoryNameField;
    private JTextArea categoryDescriptionArea;
    private JButton addCategoryButton;
    private JButton updateCategoryButton;
    private JButton deleteCategoryButton;
    private JButton refreshCategoriesButton;

    private JTable expenseTable;
    private DefaultTableModel expenseTableModel;
    private JTextField expenseTitleField;
    private JTextArea expenseDescriptionArea;
    private JTextField expenseAmountField;
    private JComboBox<Category> categoryComboBox;
    private JButton addExpenseButton;
    private JButton updateExpenseButton;
    private JButton deleteExpenseButton;
    private JButton refreshExpensesButton;

    public ExpenseTrackerGUI() {
        this.dao = new ExpenseTrackerDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadCategories();
        loadExpenses();
        loadCategoryComboBox();
    }

    private void initializeComponents() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Cat table
        String[] categoryColumnNames = {"ID", "Name", "Description", "Created At", "Updated At"};
        categoryTableModel = new DefaultTableModel(categoryColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoryTable = new JTable(categoryTableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Cat input fields
        categoryNameField = new JTextField(20);
        categoryDescriptionArea = new JTextArea(3, 20);
        categoryDescriptionArea.setLineWrap(true);
        categoryDescriptionArea.setWrapStyleWord(true);

        // Cat buttons
        addCategoryButton = new JButton("Add Category");
        updateCategoryButton = new JButton("Update Category");
        deleteCategoryButton = new JButton("Delete Category");
        refreshCategoriesButton = new JButton("Refresh");

        // Exp table
        String[] expenseColumnNames = {"ID", "Title", "Description", "Amount", "Category", "Created At", "Updated At"};
        expenseTableModel = new DefaultTableModel(expenseColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        expenseTable = new JTable(expenseTableModel);
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Expense input fields
        expenseTitleField = new JTextField(20);
        expenseDescriptionArea = new JTextArea(3, 20);
        expenseDescriptionArea.setLineWrap(true);
        expenseDescriptionArea.setWrapStyleWord(true);
        expenseAmountField = new JTextField(20);
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setRenderer(new DefaultListCellRenderer());

        // Exp buttons
        addExpenseButton = new JButton("Add Expense");
        updateExpenseButton = new JButton("Update Expense");
        deleteExpenseButton = new JButton("Delete Expense");
        refreshExpensesButton = new JButton("Refresh");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Categories tab
        JPanel categoriesPanel = createCategoriesPanel();
        tabbedPane.addTab("Categories", categoriesPanel);

        // Expenses tab
        JPanel expensesPanel = createExpensesPanel();
        tabbedPane.addTab("Expenses", expensesPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Use tabs to manage Categories and Expenses"));
        add(statusPanel, BorderLayout.SOUTH);
    }

    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(categoryNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JScrollPane(categoryDescriptionArea), gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addCategoryButton);
        buttonPanel.add(updateCategoryButton);
        buttonPanel.add(deleteCategoryButton);
        buttonPanel.add(refreshCategoriesButton);

        // North panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(categoryTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createExpensesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(expenseTitleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JScrollPane(expenseDescriptionArea), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(expenseAmountField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(categoryComboBox, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addExpenseButton);
        buttonPanel.add(updateExpenseButton);
        buttonPanel.add(deleteExpenseButton);
        buttonPanel.add(refreshExpensesButton);

        // North panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(expenseTable), BorderLayout.CENTER);

        return panel;
    }

    private void setupEventListeners() {
        // Category event listeners
        addCategoryButton.addActionListener(e -> addCategory());
        updateCategoryButton.addActionListener(e -> updateCategory());
        deleteCategoryButton.addActionListener(e -> deleteCategory());
        refreshCategoriesButton.addActionListener(e -> {
            loadCategories();
            loadCategoryComboBox();
        });

        categoryTable.getSelectionModel().addListSelectionListener(
            e -> { if (!e.getValueIsAdjusting()) loadSelectedCategory(); }
        );

        // Expense event listeners
        addExpenseButton.addActionListener(e -> addExpense());
        updateExpenseButton.addActionListener(e -> updateExpense());
        deleteExpenseButton.addActionListener(e -> deleteExpense());
        refreshExpensesButton.addActionListener(e -> loadExpenses());

        expenseTable.getSelectionModel().addListSelectionListener(
            e -> { if (!e.getValueIsAdjusting()) loadSelectedExpense(); }
        );
    }

    // Category methods
    private void addCategory() {
        String name = categoryNameField.getText().trim();
        String description = categoryDescriptionArea.getText().trim();

        if (name.isEmpty()) {
            showMessage("Category name is required!");
            return;
        }

        try {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            dao.createCategory(category);
            clearCategoryFields();
            loadCategories();
            loadCategoryComboBox();
            showMessage("Category added successfully!");
        } catch (SQLException e) {
            showError("Error adding category: " + e.getMessage());
        }
    }

    private void updateCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            showMessage("Please select a category to update!");
            return;
        }

        String name = categoryNameField.getText().trim();
        if (name.isEmpty()) {
            showMessage("Category name is required!");
            return;
        }

        int id = (int) categoryTable.getValueAt(row, 0);
        String description = categoryDescriptionArea.getText().trim();

        try {
            Category category = dao.getCategoryById(id);
            if (category != null) {
                category.setName(name);
                category.setDescription(description);
                dao.updateCategory(category);
                clearCategoryFields();
                loadCategories();
                loadCategoryComboBox();
                showMessage("Category updated successfully!");
            }
        } catch (SQLException e) {
            showError("Error updating category: " + e.getMessage());
        }
    }

    private void deleteCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            showMessage("Please select a category to delete!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this category?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            int id = (int) categoryTable.getValueAt(row, 0);
            try {
                dao.deleteCategory(id);
                clearCategoryFields();
                loadCategories();
                loadCategoryComboBox();
                showMessage("Category deleted successfully!");
            } catch (SQLException e) {
                showError("Error deleting category: " + e.getMessage());
            }
        }
    }

    private void loadCategories() {
        try {
            List<Category> categories = dao.getAllCategories();
            updateCategoryTable(categories);
        } catch (SQLException e) {
            showError("Error loading categories: " + e.getMessage());
        }
    }

    private void updateCategoryTable(List<Category> categories) {
        categoryTableModel.setRowCount(0);
        for (Category c : categories) {
            Object[] row = {
                c.getId(),
                c.getName(),
                c.getDescription(),
                c.getCreated_at(),
                c.getUpdated_at()
            };
            categoryTableModel.addRow(row);
        }
    }

    private void loadSelectedCategory() {
        int row = categoryTable.getSelectedRow();
        if (row != -1) {
            categoryNameField.setText(categoryTableModel.getValueAt(row, 1).toString());
            Object desc = categoryTableModel.getValueAt(row, 2);
            categoryDescriptionArea.setText(desc != null ? desc.toString() : "");
        }
    }

    private void clearCategoryFields() {
        categoryNameField.setText("");
        categoryDescriptionArea.setText("");
        categoryTable.clearSelection();
    }

    // Expense methods
    private void addExpense() {
        String title = expenseTitleField.getText().trim();
        String description = expenseDescriptionArea.getText().trim();
        String amountText = expenseAmountField.getText().trim();

        if (title.isEmpty()) {
            showMessage("Expense title is required!");
            return;
        }

        if (amountText.isEmpty()) {
            showMessage("Expense amount is required!");
            return;
        }

        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        if (selectedCategory == null) {
            showMessage("Please select a category!");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountText);
            Expense expense = new Expense();
            expense.setTitle(title);
            expense.setDescription(description);
            expense.setAmount(amount);
            expense.setCategoryId(selectedCategory.getId());
            
            dao.createExpense(expense);
            clearExpenseFields();
            loadExpenses();
            showMessage("Expense added successfully!");
        } catch (NumberFormatException e) {
            showError("Invalid amount format!");
        } catch (SQLException e) {
            showError("Error adding expense: " + e.getMessage());
        }
    }

    private void updateExpense() {
        int row = expenseTable.getSelectedRow();
        if (row == -1) {
            showMessage("Please select an expense to update!");
            return;
        }

        String title = expenseTitleField.getText().trim();
        String description = expenseDescriptionArea.getText().trim();
        String amountText = expenseAmountField.getText().trim();

        if (title.isEmpty()) {
            showMessage("Expense title is required!");
            return;
        }

        if (amountText.isEmpty()) {
            showMessage("Expense amount is required!");
            return;
        }

        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        if (selectedCategory == null) {
            showMessage("Please select a category!");
            return;
        }

        int id = (int) expenseTable.getValueAt(row, 0);

        try {
            BigDecimal amount = new BigDecimal(amountText);
            Expense expense = dao.getExpenseById(id);
            if (expense != null) {
                expense.setTitle(title);
                expense.setDescription(description);
                expense.setAmount(amount);
                expense.setCategoryId(selectedCategory.getId());
                
                dao.updateExpense(expense);
                clearExpenseFields();
                loadExpenses();
                showMessage("Expense updated successfully!");
            }
        } catch (NumberFormatException e) {
            showError("Invalid amount format!");
        } catch (SQLException e) {
            showError("Error updating expense: " + e.getMessage());
        }
    }

    private void deleteExpense() {
        int row = expenseTable.getSelectedRow();
        if (row == -1) {
            showMessage("Please select an expense to delete!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this expense?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            int id = (int) expenseTable.getValueAt(row, 0);
            try {
                dao.deleteExpense(id);
                clearExpenseFields();
                loadExpenses();
                showMessage("Expense deleted successfully!");
            } catch (SQLException e) {
                showError("Error deleting expense: " + e.getMessage());
            }
        }
    }

    private void loadExpenses() {
        try {
            List<Expense> expenses = dao.getAllExpenses();
            updateExpenseTable(expenses);
        } catch (SQLException e) {
            showError("Error loading expenses: " + e.getMessage());
        }
    }

    private void updateExpenseTable(List<Expense> expenses) {
        expenseTableModel.setRowCount(0);
        for (Expense e : expenses) {
            Object[] row = {
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getAmount(),
                e.getCategoryName(),
                e.getCreated_at(),
                e.getUpdated_at()
            };
            expenseTableModel.addRow(row);
        }
    }

    private void loadSelectedExpense() {
        int row = expenseTable.getSelectedRow();
        if (row != -1) {
            expenseTitleField.setText(expenseTableModel.getValueAt(row, 1).toString());
            Object desc = expenseTableModel.getValueAt(row, 2);
            expenseDescriptionArea.setText(desc != null ? desc.toString() : "");
            expenseAmountField.setText(expenseTableModel.getValueAt(row, 3).toString());
            
            // Set category combo box
            String categoryName = expenseTableModel.getValueAt(row, 4).toString();
            for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
                Category category = categoryComboBox.getItemAt(i);
                if (category.getName().equals(categoryName)) {
                    categoryComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void clearExpenseFields() {
        expenseTitleField.setText("");
        expenseDescriptionArea.setText("");
        expenseAmountField.setText("");
        categoryComboBox.setSelectedIndex(-1);
        expenseTable.clearSelection();
    }

    private void loadCategoryComboBox() {
        try {
            List<Category> categories = dao.getAllCategories();
            categoryComboBox.removeAllItems();
            for (Category category : categories) {
                categoryComboBox.addItem(category);
            }
        } catch (SQLException e) {
            showError("Error loading categories for combo box: " + e.getMessage());
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}