package com.auca.abstracts;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;

import java.time.LocalDate;

public abstract class AcademicMember extends Person {

    private String memberNumber;
    private Department department;
    private LocalDate joinDate;
    private String academicStatus;
    private String institutionalEmail;
    private String advisorName;

    public AcademicMember(String personId, String firstName, String lastName,
                          String email, String phoneNumber, LocalDate dateOfBirth,
                          String memberNumber, Department department, LocalDate joinDate,
                          String academicStatus, String institutionalEmail,
                          String advisorName) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.memberNumber = memberNumber == null ? null : memberNumber.trim();
        this.department = department;
        this.joinDate = joinDate;
        this.academicStatus = academicStatus == null ? null : academicStatus.trim().toUpperCase();
        this.institutionalEmail = institutionalEmail == null ? null : institutionalEmail.trim();
        this.advisorName = advisorName == null ? null : advisorName.trim();
    }

    // Every subclass (Student, Teacher) must give its own category
    public abstract String getAcademicCategory();

    // ---------- Identifiable ----------
    @Override
    public boolean isActive() {
        return "ACTIVE".equals(academicStatus);
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        super.validate();   // first check the 6 Person fields

        if (memberNumber == null || memberNumber.length() < MIN_ID_LENGTH
                || memberNumber.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Member number must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (department == null) {
            throw new ValidationException("A department must be selected.");
        }
        if (joinDate == null || joinDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Join date is required and cannot be in the future.");
        }
        if (!"ACTIVE".equals(academicStatus) && !"INACTIVE".equals(academicStatus)) {
            throw new ValidationException("Academic status must be ACTIVE or INACTIVE.");
        }
        if (!isValidEmail(institutionalEmail)) {
            throw new ValidationException("Invalid institutional email address.");
        }
        if (advisorName == null || advisorName.length() < MIN_NAME_LENGTH
                || advisorName.length() > 120) {
            throw new ValidationException("Advisor name must be 2-120 characters.");
        }
    }

    @Override
    public void printDetails() {
        super.printDetails();   // print the Person fields first
        System.out.println("Member number : " + memberNumber);
        System.out.println("Department    : " + (department == null ? "None"
                : "[" + department.getId() + "] " + department.getDisplayName()));
        System.out.println("Join date     : " + joinDate);
        System.out.println("Status        : " + academicStatus);
        System.out.println("Inst. email   : " + institutionalEmail);
        System.out.println("Advisor       : " + advisorName);
    }

    // ---------- Getters and setters ----------
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber == null ? null : memberNumber.trim();
    }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public String getAcademicStatus() { return academicStatus; }
    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus == null ? null : academicStatus.trim().toUpperCase();
    }

    public String getInstitutionalEmail() { return institutionalEmail; }
    public void setInstitutionalEmail(String institutionalEmail) {
        this.institutionalEmail = institutionalEmail == null ? null : institutionalEmail.trim();
    }

    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName == null ? null : advisorName.trim();
    }
}