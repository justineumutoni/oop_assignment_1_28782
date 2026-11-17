package com.auca.course;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;

public class Course implements Identifiable, Registrable {

    private static final int MIN_CREDIT_HOURS = 1;
    private static final int MAX_CREDIT_HOURS = 6;

    private String courseId;
    private String courseCode;
    private String courseTitle;
    private int creditHours;
    private Department department;
    private Teacher assignedTeacher;

    public Course(String courseId, String courseCode, String courseTitle,
                  int creditHours, Department department, Teacher assignedTeacher) {
        this.courseId = clean(courseId);
        this.courseCode = clean(courseCode) == null ? null : clean(courseCode).toUpperCase();
        this.courseTitle = clean(courseTitle);
        this.creditHours = creditHours;
        this.department = department;
        this.assignedTeacher = assignedTeacher;
    }

    // ---------- Identifiable ----------
    @Override
    public String getId() {
        return courseId;
    }

    @Override
    public String getDisplayName() {
        return courseCode + " - " + courseTitle;
    }

    @Override
    public boolean isActive() {
        return true;   // a course has no status field, so it is always active
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        if (courseId == null || courseId.length() < MIN_ID_LENGTH
                || courseId.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Course ID must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (courseCode == null || courseCode.length() < MIN_ID_LENGTH
                || courseCode.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Course code must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (courseTitle == null || courseTitle.length() < MIN_NAME_LENGTH
                || courseTitle.length() > 120) {
            throw new ValidationException("Course title must be 2-120 characters.");
        }
        if (creditHours < MIN_CREDIT_HOURS || creditHours > MAX_CREDIT_HOURS) {
            throw new ValidationException("Credit hours must be between "
                    + MIN_CREDIT_HOURS + " and " + MAX_CREDIT_HOURS + ".");
        }
        if (department == null) {
            throw new ValidationException("A department must be selected.");
        }
        if (assignedTeacher == null) {
            throw new ValidationException("A teacher must be assigned.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("Course ID     : " + courseId);
        System.out.println("Course code   : " + courseCode);
        System.out.println("Title         : " + courseTitle);
        System.out.println("Credit hours  : " + creditHours);
        System.out.println("Department    : " + (department == null ? "None"
                : department.getRegistrationSummary()));
        System.out.println("Teacher       : " + (assignedTeacher == null ? "None"
                : assignedTeacher.getDisplayName()));
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + courseId + "] " + courseCode + " - " + courseTitle
                + " (" + creditHours + " credits)";
    }

    // ---------- Helper ----------
    private static String clean(String value) {
        return value == null ? null : value.trim();
    }

    // ---------- Getters and setters ----------
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = clean(courseId); }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) {
        this.courseCode = clean(courseCode) == null ? null : clean(courseCode).toUpperCase();
    }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = clean(courseTitle); }

    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public Teacher getAssignedTeacher() { return assignedTeacher; }
    public void setAssignedTeacher(Teacher assignedTeacher) { this.assignedTeacher = assignedTeacher; }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}