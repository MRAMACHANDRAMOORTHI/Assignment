import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Model class representing a Book
class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String availabilityStatus; // "Available" or "Checked Out"

    public Book(int id, String title, String author, String genre, String availabilityStatus) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getGenre() {
        return genre;
    }
    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    @Override
    public String toString() {
        return "Book ID: " + id +
                ", Title: " + title +
                ", Author: " + author +
                ", Genre: " + genre +
                ", Status: " + availabilityStatus;
    }
}

// Main class handling the application logic
public class LibraryManagementSystem {
    private static List<Book> books = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = 0;
        do {
            printMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    searchBook();
                    break;
                case 4:
                    updateBook();
                    break;
                case 5:
                    deleteBook();
                    break;
                case 6:
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Please choose a valid option between 1 and 6.");
            }
        } while (choice != 6);
    }

    // Display the main menu options
    private static void printMenu() {
        System.out.println("\n----- Library Management System -----");
        System.out.println("1. Add a Book");
        System.out.println("2. View All Books");
        System.out.println("3. Search Book by ID or Title");
        System.out.println("4. Update Book Details");
        System.out.println("5. Delete a Book Record");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    // Add a new book with validations
    private static void addBook() {
        try {
            System.out.print("Enter Book ID (integer): ");
            int id = Integer.parseInt(scanner.nextLine());
            // Check for unique Book ID
            if (findBookById(id) != null) {
                System.out.println("Book ID already exists. Please use a unique ID.");
                return;
            }
            System.out.print("Enter Title: ");
            String title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty.");
                return;
            }
            System.out.print("Enter Author: ");
            String author = scanner.nextLine().trim();
            if (author.isEmpty()) {
                System.out.println("Author cannot be empty.");
                return;
            }
            System.out.print("Enter Genre: ");
            String genre = scanner.nextLine().trim();
            System.out.print("Enter Availability Status (Available/Checked Out): ");
            String status = scanner.nextLine().trim();
            if (!status.equalsIgnoreCase("Available") && !status.equalsIgnoreCase("Checked Out")) {
                System.out.println("Availability status must be either 'Available' or 'Checked Out'.");
                return;
            }
            Book book = new Book(id, title, author, genre, capitalizeStatus(status));
            books.add(book);
            System.out.println("Book added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for Book ID. It should be an integer.");
        }
    }

    // Display all books in the system
    private static void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the system.");
        } else {
            System.out.println("\n----- List of Books -----");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    // Search for a book by either ID or Title
    private static void searchBook() {
        System.out.println("Search by: 1. ID  2. Title");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            System.out.print("Enter Book ID: ");
            try {
                int id = Integer.parseInt(scanner.nextLine());
                Book book = findBookById(id);
                if (book != null) {
                    System.out.println("Book found: ");
                    System.out.println(book);
                } else {
                    System.out.println("No book found with the provided ID.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Book ID should be an integer.");
            }
        } else if (choice.equals("2")) {
            System.out.print("Enter Title to search: ");
            String title = scanner.nextLine().trim();
            boolean found = false;
            for (Book book : books) {
                if (book.getTitle().equalsIgnoreCase(title)) {
                    System.out.println("Book found: ");
                    System.out.println(book);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No book found with the provided title.");
            }
        } else {
            System.out.println("Invalid choice. Please select 1 or 2.");
        }
    }

    // Update details of an existing book
    private static void updateBook() {
        System.out.print("Enter the Book ID to update: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Book book = findBookById(id);
            if (book == null) {
                System.out.println("No book found with the provided ID.");
                return;
            }
            System.out.println("Leave a field blank if you do not wish to update it.");
            System.out.print("Enter new Title: ");
            String newTitle = scanner.nextLine().trim();
            if (!newTitle.isEmpty()) {
                book.setTitle(newTitle);
            }
            System.out.print("Enter new Author: ");
            String newAuthor = scanner.nextLine().trim();
            if (!newAuthor.isEmpty()) {
                book.setAuthor(newAuthor);
            }
            System.out.print("Enter new Genre: ");
            String newGenre = scanner.nextLine().trim();
            if (!newGenre.isEmpty()) {
                book.setGenre(newGenre);
            }
            System.out.print("Enter new Availability Status (Available/Checked Out): ");
            String newStatus = scanner.nextLine().trim();
            if (!newStatus.isEmpty()) {
                if (!newStatus.equalsIgnoreCase("Available") && !newStatus.equalsIgnoreCase("Checked Out")) {
                    System.out.println("Invalid availability status. Update aborted.");
                    return;
                } else {
                    book.setAvailabilityStatus(capitalizeStatus(newStatus));
                }
            }
            System.out.println("Book details updated successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Book ID should be an integer.");
        }
    }

    // Delete a book from the system
    private static void deleteBook() {
        System.out.print("Enter the Book ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Book book = findBookById(id);
            if (book != null) {
                books.remove(book);
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("No book found with the provided ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Book ID should be an integer.");
        }
    }

    // Helper method to find a book by its ID
    private static Book findBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    // Helper method to ensure proper capitalization of status
    private static String capitalizeStatus(String status) {
        if (status.equalsIgnoreCase("available")) {
            return "Available";
        } else if (status.equalsIgnoreCase("checked out")) {
            return "Checked Out";
        }
        return status;
    }
}
