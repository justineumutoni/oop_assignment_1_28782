package com.auca.student;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;

import java.time.LocalDate;

public class Student extends AcademicMember {

    // Change MAX_STUDY_YEAR to 8 if your lecturer confirms 1-8
    private static final int MIN_STUDY_YEAR = 1;
    private static final int MAX_STUDY_YEAR = 6;

    private String studentNumber;
    private String programName;
    private LocalDate admissionDate;
    private int studyYear;
    private String guardianPhone;
    private boolean scholarshipStatus;

    public Student(String personId, String firstName, String lastName,
                   String email, String phoneNumber, LocalDate dateOfBirth,
                   String memberNumber, Department department, LocalDate joinDate,
                   String academicStatus, String institutionalEmail, String advisorName,
                   String studentNumber, String programName, LocalDate admissionDate,
                   int studyYear, String guardianPhone, boolean scholarshipStatus) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth,
                memberNumber, department, joinDate, academicStatus,
                institutionalEmail, advisorName);
        this.studentNumber = studentNumber == null ? null : studentNumber.trim();
        this.programName = programName == null ? null : programName.trim();
        this.admissionDate = admissionDate;
        this.studyYear = studyYear;
        this.guardianPhone = guardianPhone == null ? null : guardianPhone.trim();
        this.scholarshipStatus = scholarshipStatus;
    }

    // ---------- Required abstract methods ----------
    @Override
    public String getRoleDescription() {
        return "Student";
    }

    @Override
    public String getAcademicCategory() {
        return "Learner";
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        super.validate();   // Person fields, then AcademicMember fields

        if (studentNumber == null || studentNumber.length() < MIN_ID_LENGTH
                || studentNumber.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Student number must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (programName == null || programName.length() < MIN_NAME_LENGTH
                || programName.length() > 120) {
            throw new ValidationException("Program name must be 2-120 characters.");
        }
        if (admissionDate == null || admissionDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Admission date is required and cannot be in the future.");
        }
        if (studyYear < MIN_STUDY_YEAR || studyYear > MAX_STUDY_YEAR) {
            throw new ValidationException("Study year must be between " + MIN_STUDY_YEAR
                    + " and " + MAX_STUDY_YEAR + ".");
        }
        if (!isValidPhone(guardianPhone)) {
            throw new ValidationException("Guardian phone must have 10-15 digits, optional leading +.");
        }
    }

    @Override
    public void printDetails() {
        super.printDetails();   // Person + AcademicMember fields
        System.out.println("Student no.   : " + studentNumber);
        System.out.println("Program       : " + programName);
        System.out.println("Admission date: " + admissionDate);
        System.out.println("Study year    : " + studyYear);
        System.out.println("Guardian phone: " + guardianPhone);
        System.out.println("Scholarship   : " + (scholarshipStatus ? "YES" : "NO"));
    }

    // ---------- Getters and setters ----------
    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber == null ? null : studentNumber.trim();
    }

    public String getProgramName() { return programName; }
    public void setProgramName(String programName) {
        this.programName = programName == null ? null : programName.trim();
    }

    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }

    public int getStudyYear() { return studyYear; }
    public void setStudyYear(int studyYear) { this.studyYear = studyYear; }

    public String getGuardianPhone() { return guardianPhone; }
    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone == null ? null : guardianPhone.trim();
    }

    public boolean isScholarshipStatus() { return scholarshipStatus; }
    public void setScholarshipStatus(boolean scholarshipStatus) {
        this.scholarshipStatus = scholarshipStatus;
    }

    @Override
    public String toString() {
        return getRegistrationSummary() + " | " + programName + " | Year " + studyYear;
    }
}