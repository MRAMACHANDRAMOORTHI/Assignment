package msr;

import javafx.beans.property.*;

public class Book {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty genre;
    private final StringProperty availabilityStatus; // "Available" or "Checked Out"

    public Book(int id, String title, String author, String genre, String availabilityStatus) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.availabilityStatus = new SimpleStringProperty(availabilityStatus);
    }

    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }
    public IntegerProperty idProperty() {
        return id;
    }

    public String getTitle() {
        return title.get();
    }
    public void setTitle(String title) {
        this.title.set(title);
    }
    public StringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }
    public void setAuthor(String author) {
        this.author.set(author);
    }
    public StringProperty authorProperty() {
        return author;
    }

    public String getGenre() {
        return genre.get();
    }
    public void setGenre(String genre) {
        this.genre.set(genre);
    }
    public StringProperty genreProperty() {
        return genre;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus.get();
    }
    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus.set(availabilityStatus);
    }
    public StringProperty availabilityStatusProperty() {
        return availabilityStatus;
    }
}
