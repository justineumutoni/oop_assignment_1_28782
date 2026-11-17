package com.auca.librarybook;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

public class LibraryBook implements Identifiable, Registrable {

    private String bookId;
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    public LibraryBook(String bookId, String isbn, String title, String author,
                       int totalCopies, int availableCopies) {
        this.bookId = clean(bookId);
        this.isbn = clean(isbn);
        this.title = clean(title);
        this.author = clean(author);
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    // ---------- Identifiable ----------
    @Override
    public String getId() {
        return bookId;
    }

    @Override
    public String getDisplayName() {
        return title + " by " + author;
    }

    @Override
    public boolean isActive() {
        return availableCopies > 0;   // active = at least one copy can be borrowed
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        if (bookId == null || bookId.length() < MIN_ID_LENGTH
                || bookId.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Book ID must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        // Assumption: ISBN has 10 or 13 digits; hyphens are allowed
        if (isbn == null || !isbn.replace("-", "").matches("\\d{10}|\\d{13}")) {
            throw new ValidationException("ISBN must have 10 or 13 digits (hyphens allowed).");
        }
        if (title == null || title.length() < MIN_NAME_LENGTH || title.length() > 120) {
            throw new ValidationException("Title must be 2-120 characters.");
        }
        if (author == null || author.length() < MIN_NAME_LENGTH || author.length() > 120) {
            throw new ValidationException("Author must be 2-120 characters.");
        }
        if (totalCopies < 0) {
            throw new ValidationException("Total copies cannot be negative.");
        }
        if (availableCopies < 0) {
            throw new ValidationException("Available copies cannot be negative.");
        }
        if (availableCopies > totalCopies) {
            throw new ValidationException("Available copies cannot exceed total copies.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("Book ID       : " + bookId);
        System.out.println("ISBN          : " + isbn);
        System.out.println("Title         : " + title);
        System.out.println("Author        : " + author);
        System.out.println("Total copies  : " + totalCopies);
        System.out.println("Available     : " + availableCopies);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + bookId + "] " + title + " by " + author
                + " (" + availableCopies + "/" + totalCopies + " available)";
    }

    // ---------- Helper ----------
    private static String clean(String value) {
        return value == null ? null : value.trim();
    }

    // ---------- Getters and setters ----------
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = clean(bookId); }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = clean(isbn); }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = clean(title); }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = clean(author); }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}