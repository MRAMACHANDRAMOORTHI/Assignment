import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
        import javafx.stage.Stage;

import javax.swing.text.TableView;

// Model class representing a Book
public class LibraryManagementUI extends Application {
    // Observable list to hold Book objects
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    // TableView for displaying books
    private TableView<Book> tableView;

    // Form fields for input
    private TextField idField, titleField, authorField, genreField;
    private ComboBox<String> statusBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Management System");

        // Build the table view
        tableView = new TableView<>();
        tableView.setItems(bookList);
        tableView.setPrefHeight(300);
        setupTableColumns();

        // Build the form to add/update books
        GridPane form = createFormPane();

        // Build buttons pane
        HBox buttonPane = createButtonPane();

        // Main layout
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(15));
        mainLayout.getChildren().addAll(tableView, form, buttonPane);

        // Apply external CSS (optional – see sample CSS at the end)
        Scene scene = new Scene(mainLayout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Set up TableView columns
    private void setupTableColumns() {
        TableColumn<Book, Integer> idCol = new TableColumn<>("Book ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(150);

        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setPrefWidth(120);

        TableColumn<Book, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));
        statusCol.setPrefWidth(120);

        tableView.getColumns().addAll(idCol, titleCol, authorCol, genreCol, statusCol);

        // When a row is clicked, load the details into the form
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadBookDetails(newSelection);
            }
        });
    }

    // Create form for book details input
    private GridPane createFormPane() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        Label idLabel = new Label("Book ID:");
        idField = new TextField();
        Label titleLabel = new Label("Title:");
        titleField = new TextField();
        Label authorLabel = new Label("Author:");
        authorField = new TextField();
        Label genreLabel = new Label("Genre:");
        genreField = new TextField();
        Label statusLabel = new Label("Status:");
        statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Available", "Checked Out");
        statusBox.setValue("Available");

        form.add(idLabel, 0, 0);
        form.add(idField, 1, 0);
        form.add(titleLabel, 0, 1);
        form.add(titleField, 1, 1);
        form.add(authorLabel, 0, 2);
        form.add(authorField, 1, 2);
        form.add(genreLabel, 0, 3);
        form.add(genreField, 1, 3);
        form.add(statusLabel, 0, 4);
        form.add(statusBox, 1, 4);

        return form;
    }

    // Create buttons pane for actions
    private HBox createButtonPane() {
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addBook());
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateBook());
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteBook());
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> clearForm());

        HBox buttonPane = new HBox(10);
        buttonPane.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);
        return buttonPane;
    }

    // Load selected book details into the form fields
    private void loadBookDetails(Book book) {
        idField.setText(String.valueOf(book.getId()));
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        genreField.setText(book.getGenre());
        statusBox.setValue(book.getAvailabilityStatus());
    }

    // Clear the form fields
    private void clearForm() {
        idField.clear();
        titleField.clear();
        authorField.clear();
        genreField.clear();
        statusBox.setValue("Available");
        tableView.getSelectionModel().clearSelection();
    }

    // Add a new book from the form data
    private void addBook() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            // Check if the book ID is unique
            if (findBookById(id) != null) {
                showAlert(Alert.AlertType.ERROR, "Duplicate ID", "A book with this ID already exists.");
                return;
            }
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String status = statusBox.getValue();

            if (title.isEmpty() || author.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Title and Author cannot be empty.");
                return;
            }

            Book newBook = new Book(id, title, author, genre, status);
            bookList.add(newBook);
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully!");
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Book ID must be an integer.");
        }
    }

    // Update the selected book with form data
    private void updateBook() {
        Book selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No book selected to update.");
            return;
        }
        try {
            int id = Integer.parseInt(idField.getText().trim());
            // Ensure that if the ID is changed, it doesn't duplicate an existing one
            if (id != selected.getId() && findBookById(id) != null) {
                showAlert(Alert.AlertType.ERROR, "Duplicate ID", "A book with this ID already exists.");
                return;
            }
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String status = statusBox.getValue();

            if (title.isEmpty() || author.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Title and Author cannot be empty.");
                return;
            }

            selected.setId(id); // If you allow changing ID
            selected.setTitle(title);
            selected.setAuthor(author);
            selected.setGenre(genre);
            selected.setAvailabilityStatus(status);
            tableView.refresh();
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book updated successfully!");
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Book ID must be an integer.");
        }
    }

    // Delete the selected book
    private void deleteBook() {
        Book selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No book selected to delete.");
            return;
        }
        bookList.remove(selected);
        clearForm();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Book deleted successfully!");
    }

    // Find a book by its ID in the list
    private Book findBookById(int id) {
        for (Book b : bookList) {
            if (b.getId() == id)
                return b;
        }
        return null;
    }

    // Utility method to display alert dialogs
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

// Model class with JavaFX properties
class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String availabilityStatus;

    public Book(int id, String title, String author, String genre, String availabilityStatus) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters and setters (for JavaFX PropertyValueFactory, standard getters/setters suffice)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getAvailabilityStatus() {
        return availabilityStatus;
    }
    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
