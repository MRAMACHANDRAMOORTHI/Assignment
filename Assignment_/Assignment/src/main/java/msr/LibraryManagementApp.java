package msr;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LibraryManagementApp extends Application {
    private TableView<Book> table;
    private ObservableList<Book> bookList;

    // Input fields
    private TextField idField;
    private TextField titleField;
    private TextField authorField;
    private TextField genreField;
    private TextField availabilityField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Management System");

        // Initialize the book list
        bookList = FXCollections.observableArrayList();

        // Create the TableView and its columns
        table = new TableView<>();
        table.setItems(bookList);
        table.setPrefWidth(600);
        table.setPrefHeight(300);

        TableColumn<Book, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(150);

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(100);

        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setPrefWidth(100);

        TableColumn<Book, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, titleCol, authorCol, genreCol, statusCol);

        // Create input fields with prompt text
        idField = new TextField();
        idField.setPromptText("ID");
        idField.setPrefWidth(50);

        titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setPrefWidth(150);

        authorField = new TextField();
        authorField.setPromptText("Author");
        authorField.setPrefWidth(100);

        genreField = new TextField();
        genreField.setPromptText("Genre");
        genreField.setPrefWidth(100);

        availabilityField = new TextField();
        availabilityField.setPromptText("Available/Checked Out");
        availabilityField.setPrefWidth(100);

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(idField, titleField, authorField, genreField, availabilityField);
        inputBox.setAlignment(Pos.CENTER);

        // Create buttons and attach actions
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addBook());

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateBook());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteBook());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Layout: Combine table, inputs, and buttons
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));
        vbox.getChildren().addAll(table, inputBox, buttonBox);

        Scene scene = new Scene(vbox, 800, 500);
        // Optional: Add external CSS for a more polished look by adding scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Add a new book to the list
    private void addBook() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            // Check for unique Book ID
            for (Book b : bookList) {
                if (b.getId() == id) {
                    showAlert("Duplicate ID", "Book ID already exists.");
                    return;
                }
            }
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String status = availabilityField.getText().trim();
            if (title.isEmpty() || author.isEmpty()) {
                showAlert("Input Error", "Title and Author cannot be empty.");
                return;
            }
            if (!status.equalsIgnoreCase("Available") && !status.equalsIgnoreCase("Checked Out")) {
                showAlert("Input Error", "Availability must be 'Available' or 'Checked Out'.");
                return;
            }
            Book book = new Book(id, title, author, genre, capitalizeStatus(status));
            bookList.add(book);
            clearInputs();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "ID must be an integer.");
        }
    }

    // Update the selected book with new details
    private void updateBook() {
        Book selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "No book selected. Please select a book from the table.");
            return;
        }
        // Update fields only if input is provided
        String newTitle = titleField.getText().trim();
        if (!newTitle.isEmpty()) {
            selected.setTitle(newTitle);
        }
        String newAuthor = authorField.getText().trim();
        if (!newAuthor.isEmpty()) {
            selected.setAuthor(newAuthor);
        }
        String newGenre = genreField.getText().trim();
        if (!newGenre.isEmpty()) {
            selected.setGenre(newGenre);
        }
        String newStatus = availabilityField.getText().trim();
        if (!newStatus.isEmpty()) {
            if (!newStatus.equalsIgnoreCase("Available") && !newStatus.equalsIgnoreCase("Checked Out")) {
                showAlert("Input Error", "Availability must be 'Available' or 'Checked Out'.");
                return;
            }
            selected.setAvailabilityStatus(capitalizeStatus(newStatus));
        }
        table.refresh();
        clearInputs();
    }

    // Delete the selected book from the list
    private void deleteBook() {
        Book selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "No book selected. Please select a book from the table.");
            return;
        }
        bookList.remove(selected);
        clearInputs();
    }

    // Helper method to capitalize status text
    private String capitalizeStatus(String status) {
        if (status.equalsIgnoreCase("available"))
            return "Available";
        else if (status.equalsIgnoreCase("checked out"))
            return "Checked Out";
        return status;
    }

    // Clear input fields after an action
    private void clearInputs() {
        idField.clear();
        titleField.clear();
        authorField.clear();
        genreField.clear();
        availabilityField.clear();
    }

    // Show an alert dialog with given title and message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
