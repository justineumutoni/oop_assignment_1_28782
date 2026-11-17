package com.auca.abstracts;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

import java.time.LocalDate;

public abstract class Person implements Identifiable, Registrable {

    private String personId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    public Person(String personId, String firstName, String lastName,
                  String email, String phoneNumber, LocalDate dateOfBirth) {
        this.personId = clean(personId);
        this.firstName = clean(firstName);
        this.lastName = clean(lastName);
        this.email = clean(email);
        this.phoneNumber = clean(phoneNumber);
        this.dateOfBirth = dateOfBirth;
    }

    // Every subclass must say what its role is
    public abstract String getRoleDescription();

    // ---------- Identifiable ----------
    @Override
    public String getId() {
        return personId;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    // isActive() is NOT written here. Student/Teacher and Administrator
    // each decide it differently, so they will implement it.

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        if (personId == null || personId.length() < MIN_ID_LENGTH
                || personId.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Person ID must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        checkName(firstName, "First name");
        checkName(lastName, "Last name");
        if (!isValidEmail(email)) {
            throw new ValidationException("Invalid email address.");
        }
        if (!isValidPhone(phoneNumber)) {
            throw new ValidationException("Phone must have 10-15 digits, optional leading +.");
        }
        if (dateOfBirth == null || !dateOfBirth.isBefore(LocalDate.now())) {
            throw new ValidationException("Date of birth must be in the past.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("Person ID     : " + personId);
        System.out.println("First name    : " + firstName);
        System.out.println("Last name     : " + lastName);
        System.out.println("Email         : " + email);
        System.out.println("Phone         : " + phoneNumber);
        System.out.println("Date of birth : " + dateOfBirth);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + personId + "] " + getDisplayName() + " (" + getRoleDescription() + ")";
    }

    // ---------- Helpers (reused by subclasses) ----------
    protected static boolean isValidEmail(String value) {
        return value != null
                && value.length() <= MAX_EMAIL_LENGTH
                && value.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    protected static boolean isValidPhone(String value) {
        return value != null && value.matches("^\\+?\\d{" + MIN_PHONE_LENGTH + ","
                + MAX_PHONE_LENGTH + "}$");
    }

    private static String clean(String value) {
        return value == null ? null : value.trim();
    }

    private static void checkName(String value, String label) throws ValidationException {
        if (value == null || value.length() < MIN_NAME_LENGTH
                || value.length() > MAX_NAME_LENGTH) {
            throw new ValidationException(label + " must be " + MIN_NAME_LENGTH
                    + "-" + MAX_NAME_LENGTH + " characters.");
        }
    }

    // ---------- Getters and setters ----------
    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = clean(personId); }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = clean(firstName); }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = clean(lastName); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = clean(email); }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = clean(phoneNumber); }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}