package com.auca.department;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;

public class Department implements Identifiable, Registrable {

    private String departmentId;
    private String departmentName;
    private String departmentCode;
    private String officeLocation;
    private String contactEmail;
    private Teacher headTeacher;   // optional: can be null at creation

    public Department(String departmentId, String departmentName, String departmentCode,
                      String officeLocation, String contactEmail, Teacher headTeacher) {
        this.departmentId = clean(departmentId);
        this.departmentName = clean(departmentName);
        this.departmentCode = clean(departmentCode) == null ? null : clean(departmentCode).toUpperCase();
        this.officeLocation = clean(officeLocation);
        this.contactEmail = clean(contactEmail);
        this.headTeacher = headTeacher;
    }

    // ---------- Identifiable ----------
    @Override
    public String getId() {
        return departmentId;
    }

    @Override
    public String getDisplayName() {
        return departmentName;
    }

    @Override
    public boolean isActive() {
        return true;   // a department has no status field, so it is always active
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        if (departmentId == null || departmentId.length() < MIN_ID_LENGTH
                || departmentId.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Department ID must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (departmentName == null || departmentName.length() < MIN_NAME_LENGTH
                || departmentName.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Department name must be " + MIN_NAME_LENGTH
                    + "-" + MAX_NAME_LENGTH + " characters.");
        }
        if (departmentCode == null || departmentCode.length() < 2
                || departmentCode.length() > 10) {
            throw new ValidationException("Department code must be 2-10 characters.");
        }
        if (officeLocation == null || officeLocation.isEmpty()) {
            throw new ValidationException("Office location cannot be blank.");
        }
        if (contactEmail == null || contactEmail.length() > MAX_EMAIL_LENGTH
                || !contactEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ValidationException("Invalid contact email address.");
        }
        // headTeacher is optional, so it is not checked here
    }

    @Override
    public void printDetails() {
        System.out.println("Department ID : " + departmentId);
        System.out.println("Name          : " + departmentName);
        System.out.println("Code          : " + departmentCode);
        System.out.println("Office        : " + officeLocation);
        System.out.println("Contact email : " + contactEmail);
        System.out.println("Head teacher  : " + (headTeacher == null ? "Not assigned"
                : headTeacher.getDisplayName()));
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + departmentId + "] " + departmentName + " (" + departmentCode + ")";
    }

    // ---------- Helper ----------
    private static String clean(String value) {
        return value == null ? null : value.trim();
    }

    // ---------- Getters and setters ----------
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = clean(departmentId); }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = clean(departmentName); }

    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = clean(departmentCode) == null ? null : clean(departmentCode).toUpperCase();
    }

    public String getOfficeLocation() { return officeLocation; }
    public void setOfficeLocation(String officeLocation) { this.officeLocation = clean(officeLocation); }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = clean(contactEmail); }

    public Teacher getHeadTeacher() { return headTeacher; }
    public void setHeadTeacher(Teacher headTeacher) { this.headTeacher = headTeacher; }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}