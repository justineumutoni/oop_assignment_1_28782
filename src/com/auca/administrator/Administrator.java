package com.auca.administrator;

import com.auca.abstracts.Person;
import com.auca.exceptions.ValidationException;

import java.time.LocalDate;

public class Administrator extends Person {

    // Assumption: the assignment gives no range, so 1-5 is used
    private static final int MIN_ACCESS_LEVEL = 1;
    private static final int MAX_ACCESS_LEVEL = 5;

    private String staffNumber;
    private String jobTitle;
    private String assignedOffice;
    private LocalDate employmentDate;
    private int accessLevel;
    private boolean active;

    public Administrator(String personId, String firstName, String lastName,
                         String email, String phoneNumber, LocalDate dateOfBirth,
                         String staffNumber, String jobTitle, String assignedOffice,
                         LocalDate employmentDate, int accessLevel, boolean active) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.staffNumber = staffNumber == null ? null : staffNumber.trim();
        this.jobTitle = jobTitle == null ? null : jobTitle.trim();
        this.assignedOffice = assignedOffice == null ? null : assignedOffice.trim();
        this.employmentDate = employmentDate;
        this.accessLevel = accessLevel;
        this.active = active;
    }

    // ---------- Required abstract method ----------
    @Override
    public String getRoleDescription() {
        return "Administrator";
    }

    // ---------- Identifiable ----------
    @Override
    public boolean isActive() {
        return active;
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        super.validate();   // the 6 Person fields first

        if (staffNumber == null || staffNumber.length() < MIN_ID_LENGTH
                || staffNumber.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Staff number must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (jobTitle == null || jobTitle.length() < MIN_NAME_LENGTH
                || jobTitle.length() > 120) {
            throw new ValidationException("Job title must be 2-120 characters.");
        }
        if (assignedOffice == null || assignedOffice.isEmpty()) {
            throw new ValidationException("Assigned office cannot be blank.");
        }
        if (employmentDate == null || employmentDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Employment date is required and cannot be in the future.");
        }
        if (accessLevel < MIN_ACCESS_LEVEL || accessLevel > MAX_ACCESS_LEVEL) {
            throw new ValidationException("Access level must be between " + MIN_ACCESS_LEVEL
                    + " and " + MAX_ACCESS_LEVEL + ".");
        }
    }

    @Override
    public void printDetails() {
        super.printDetails();   // the 6 Person fields
        System.out.println("Staff number  : " + staffNumber);
        System.out.println("Job title     : " + jobTitle);
        System.out.println("Office        : " + assignedOffice);
        System.out.println("Employed on   : " + employmentDate);
        System.out.println("Access level  : " + accessLevel);
        System.out.println("Active        : " + (active ? "YES" : "NO"));
    }

    // ---------- Getters and setters ----------
    public String getStaffNumber() { return staffNumber; }
    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber == null ? null : staffNumber.trim();
    }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle == null ? null : jobTitle.trim();
    }

    public String getAssignedOffice() { return assignedOffice; }
    public void setAssignedOffice(String assignedOffice) {
        this.assignedOffice = assignedOffice == null ? null : assignedOffice.trim();
    }

    public LocalDate getEmploymentDate() { return employmentDate; }
    public void setEmploymentDate(LocalDate employmentDate) { this.employmentDate = employmentDate; }

    public int getAccessLevel() { return accessLevel; }
    public void setAccessLevel(int accessLevel) { this.accessLevel = accessLevel; }

    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return getRegistrationSummary() + " | " + jobTitle;
    }
}