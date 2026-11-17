package com.auca.assessment;

import com.auca.enrollment.Enrollment;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

import java.time.LocalDate;
import java.util.List;

public class Assessment implements Identifiable, Registrable {

    // Allowed values (Main will show these as choices)
    public static final List<String> ALLOWED_TYPES =
            List.of("QUIZ", "ASSIGNMENT", "MIDTERM", "FINAL_EXAM");

    private String assessmentId;
    private Enrollment enrollment;
    private String assessmentType;
    private double maximumScore;
    private double obtainedScore;
    private LocalDate assessmentDate;

    public Assessment(String assessmentId, Enrollment enrollment, String assessmentType,
                      double maximumScore, double obtainedScore, LocalDate assessmentDate) {
        this.assessmentId = clean(assessmentId);
        this.enrollment = enrollment;
        this.assessmentType = clean(assessmentType) == null ? null : clean(assessmentType).toUpperCase();
        this.maximumScore = maximumScore;
        this.obtainedScore = obtainedScore;
        this.assessmentDate = assessmentDate;
    }

    // ---------- Identifiable ----------
    @Override
    public String getId() {
        return assessmentId;
    }

    @Override
    public String getDisplayName() {
        return assessmentType + " - " + (enrollment == null ? "?" : enrollment.getDisplayName());
    }

    @Override
    public boolean isActive() {
        return true;   // an assessment has no status field, so it is always active
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        if (assessmentId == null || assessmentId.length() < MIN_ID_LENGTH
                || assessmentId.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Assessment ID must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (enrollment == null) {
            throw new ValidationException("An enrollment must be selected.");
        }
        if (assessmentType == null || !ALLOWED_TYPES.contains(assessmentType)) {
            throw new ValidationException("Assessment type must be one of: " + ALLOWED_TYPES);
        }
        if (maximumScore <= 0) {
            throw new ValidationException("Maximum score must be greater than zero.");
        }
        if (obtainedScore < 0 || obtainedScore > maximumScore) {
            throw new ValidationException("Obtained score must be between 0 and " + maximumScore + ".");
        }
        if (assessmentDate == null) {
            throw new ValidationException("Assessment date is required.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("Assessment ID : " + assessmentId);
        System.out.println("Enrollment    : " + (enrollment == null ? "None"
                : enrollment.getRegistrationSummary()));
        System.out.println("Type          : " + assessmentType);
        System.out.println("Maximum score : " + maximumScore);
        System.out.println("Obtained score: " + obtainedScore);
        System.out.println("Date          : " + assessmentDate);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + assessmentId + "] " + getDisplayName()
                + " : " + obtainedScore + "/" + maximumScore;
    }

    // ---------- Helper ----------
    private static String clean(String value) {
        return value == null ? null : value.trim();
    }

    // ---------- Getters and setters ----------
    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = clean(assessmentId); }

    public Enrollment getEnrollment() { return enrollment; }
    public void setEnrollment(Enrollment enrollment) { this.enrollment = enrollment; }

    public String getAssessmentType() { return assessmentType; }
    public void setAssessmentType(String assessmentType) {
        this.assessmentType = clean(assessmentType) == null ? null : clean(assessmentType).toUpperCase();
    }

    public double getMaximumScore() { return maximumScore; }
    public void setMaximumScore(double maximumScore) { this.maximumScore = maximumScore; }

    public double getObtainedScore() { return obtainedScore; }
    public void setObtainedScore(double obtainedScore) { this.obtainedScore = obtainedScore; }

    public LocalDate getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(LocalDate assessmentDate) { this.assessmentDate = assessmentDate; }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}