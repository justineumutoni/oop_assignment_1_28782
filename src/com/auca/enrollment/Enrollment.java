package com.auca.enrollment;

import com.auca.course.Course;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;

import java.time.LocalDate;
import java.util.List;

public class Enrollment implements Identifiable, Registrable {

    // Allowed values (Main will show these as choices)
    public static final List<String> ALLOWED_SEMESTERS =
            List.of("SEMESTER_1", "SEMESTER_2", "SEMESTER_3");
    public static final List<String> ALLOWED_STATUSES =
            List.of("ACTIVE", "COMPLETED", "DROPPED");

    private String enrollmentId;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private String semester;
    private String enrollmentStatus;

    public Enrollment(String enrollmentId, Student student, Course course,
                      LocalDate enrollmentDate, String semester, String enrollmentStatus) {
        this.enrollmentId = clean(enrollmentId);
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        this.semester = clean(semester) == null ? null : clean(semester).toUpperCase();
        this.enrollmentStatus = clean(enrollmentStatus) == null ? null : clean(enrollmentStatus).toUpperCase();
    }

    // ---------- Identifiable ----------
    @Override
    public String getId() {
        return enrollmentId;
    }

    @Override
    public String getDisplayName() {
        return (student == null ? "?" : student.getDisplayName())
                + " in " + (course == null ? "?" : course.getCourseCode());
    }

    @Override
    public boolean isActive() {
        return "ACTIVE".equals(enrollmentStatus);
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        if (enrollmentId == null || enrollmentId.length() < MIN_ID_LENGTH
                || enrollmentId.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Enrollment ID must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (student == null) {
            throw new ValidationException("A student must be selected.");
        }
        if (course == null) {
            throw new ValidationException("A course must be selected.");
        }
        if (enrollmentDate == null || enrollmentDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Enrollment date is required and cannot be in the future.");
        }
        if (semester == null || !ALLOWED_SEMESTERS.contains(semester)) {
            throw new ValidationException("Semester must be one of: " + ALLOWED_SEMESTERS);
        }
        if (enrollmentStatus == null || !ALLOWED_STATUSES.contains(enrollmentStatus)) {
            throw new ValidationException("Enrollment status must be one of: " + ALLOWED_STATUSES);
        }
    }

    @Override
    public void printDetails() {
        System.out.println("Enrollment ID : " + enrollmentId);
        System.out.println("Student       : " + (student == null ? "None"
                : student.getRegistrationSummary()));
        System.out.println("Course        : " + (course == null ? "None"
                : course.getRegistrationSummary()));
        System.out.println("Date          : " + enrollmentDate);
        System.out.println("Semester      : " + semester);
        System.out.println("Status        : " + enrollmentStatus);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + enrollmentId + "] " + getDisplayName()
                + " (" + semester + ", " + enrollmentStatus + ")";
    }

    // ---------- Helper ----------
    private static String clean(String value) {
        return value == null ? null : value.trim();
    }

    // ---------- Getters and setters ----------
    public String getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(String enrollmentId) { this.enrollmentId = clean(enrollmentId); }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) {
        this.semester = clean(semester) == null ? null : clean(semester).toUpperCase();
    }

    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = clean(enrollmentStatus) == null ? null : clean(enrollmentStatus).toUpperCase();
    }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}