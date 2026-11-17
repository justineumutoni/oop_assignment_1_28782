package com.auca.bookloan;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;

import java.time.LocalDate;

public class BookLoan implements Identifiable, Registrable {

    private String loanId;
    private Student student;
    private LibraryBook book;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;   // null until the book is returned

    public BookLoan(String loanId, Student student, LibraryBook book,
                    LocalDate borrowedDate, LocalDate dueDate, LocalDate returnedDate) {
        this.loanId = loanId == null ? null : loanId.trim();
        this.student = student;
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }

    // ---------- Identifiable ----------
    @Override
    public String getId() {
        return loanId;
    }

    @Override
    public String getDisplayName() {
        return (student == null ? "?" : student.getDisplayName())
                + " borrowed " + (book == null ? "?" : book.getTitle());
    }

    @Override
    public boolean isActive() {
        return returnedDate == null;   // a loan is active until the book is returned
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        if (loanId == null || loanId.length() < MIN_ID_LENGTH
                || loanId.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Loan ID must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (student == null) {
            throw new ValidationException("A student must be selected.");
        }
        if (book == null) {
            throw new ValidationException("A book must be selected.");
        }
        if (borrowedDate == null || borrowedDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Borrowed date is required and cannot be in the future.");
        }
        if (dueDate == null || !dueDate.isAfter(borrowedDate)) {
            throw new ValidationException("Due date must be after the borrowed date.");
        }
        if (returnedDate != null && returnedDate.isBefore(borrowedDate)) {
            throw new ValidationException("Returned date cannot be before the borrowed date.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("Loan ID       : " + loanId);
        System.out.println("Student       : " + (student == null ? "None"
                : student.getRegistrationSummary()));
        System.out.println("Book          : " + (book == null ? "None"
                : book.getRegistrationSummary()));
        System.out.println("Borrowed      : " + borrowedDate);
        System.out.println("Due           : " + dueDate);
        System.out.println("Returned      : " + (returnedDate == null ? "Not yet returned" : returnedDate));
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + loanId + "] " + getDisplayName() + " (due " + dueDate
                + (returnedDate == null ? ", NOT RETURNED" : ", returned " + returnedDate) + ")";
    }

    // ---------- Getters and setters ----------
    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId == null ? null : loanId.trim(); }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LibraryBook getBook() { return book; }
    public void setBook(LibraryBook book) { this.book = book; }

    public LocalDate getBorrowedDate() { return borrowedDate; }
    public void setBorrowedDate(LocalDate borrowedDate) { this.borrowedDate = borrowedDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnedDate() { return returnedDate; }
    public void setReturnedDate(LocalDate returnedDate) { this.returnedDate = returnedDate; }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}