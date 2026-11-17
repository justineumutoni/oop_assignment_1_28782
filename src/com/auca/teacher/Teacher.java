package com.auca.teacher;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Teacher extends AcademicMember {

    private String employeeNumber;
    private String specialization;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String officeNumber;
    private String employmentType;   // FULL_TIME, PART_TIME or CONTRACT

    public Teacher(String personId, String firstName, String lastName,
                   String email, String phoneNumber, LocalDate dateOfBirth,
                   String memberNumber, Department department, LocalDate joinDate,
                   String academicStatus, String institutionalEmail, String advisorName,
                   String employeeNumber, String specialization, LocalDate hireDate,
                   BigDecimal salary, String officeNumber, String employmentType) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth,
                memberNumber, department, joinDate, academicStatus,
                institutionalEmail, advisorName);
        this.employeeNumber = employeeNumber == null ? null : employeeNumber.trim();
        this.specialization = specialization == null ? null : specialization.trim();
        this.hireDate = hireDate;
        this.salary = salary;
        this.officeNumber = officeNumber == null ? null : officeNumber.trim();
        this.employmentType = employmentType == null ? null : employmentType.trim().toUpperCase();
    }

    // ---------- Required abstract methods ----------
    @Override
    public String getRoleDescription() {
        return "Teacher";
    }

    @Override
    public String getAcademicCategory() {
        return "Teaching Staff";
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        super.validate();   // Person fields, then AcademicMember fields

        if (employeeNumber == null || employeeNumber.length() < MIN_ID_LENGTH
                || employeeNumber.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Employee number must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (specialization == null || specialization.length() < MIN_NAME_LENGTH
                || specialization.length() > 120) {
            throw new ValidationException("Specialization must be 2-120 characters.");
        }
        if (hireDate == null || hireDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Hire date is required and cannot be in the future.");
        }
        if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Salary must be greater than zero.");
        }
        if (officeNumber == null || officeNumber.isEmpty()) {
            throw new ValidationException("Office number cannot be blank.");
        }
        if (!"FULL_TIME".equals(employmentType) && !"PART_TIME".equals(employmentType)
                && !"CONTRACT".equals(employmentType)) {
            throw new ValidationException(
                    "Employment type must be FULL_TIME, PART_TIME or CONTRACT.");
        }
    }

    @Override
    public void printDetails() {
        super.printDetails();   // Person + AcademicMember fields
        System.out.println("Employee no.  : " + employeeNumber);
        System.out.println("Specialization: " + specialization);
        System.out.println("Hire date     : " + hireDate);
        System.out.println("Salary        : " + salary);
        System.out.println("Office        : " + officeNumber);
        System.out.println("Employment    : " + employmentType);
    }

    // ---------- Getters and setters ----------
    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber == null ? null : employeeNumber.trim();
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) {
        this.specialization = specialization == null ? null : specialization.trim();
    }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public String getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber == null ? null : officeNumber.trim();
    }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType == null ? null : employmentType.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return getRegistrationSummary() + " | " + specialization;
    }
}