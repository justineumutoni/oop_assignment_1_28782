package com.auca.feepayment;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FeePayment implements Identifiable, Registrable {

    public static final List<String> ALLOWED_METHODS =
            List.of("CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CARD");

    private String paymentId;
    private Student student;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String receiptNumber;

    public FeePayment(String paymentId, Student student, BigDecimal amount,
                      LocalDate paymentDate, String paymentMethod, String receiptNumber) {
        this.paymentId = clean(paymentId);
        this.student = student;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = clean(paymentMethod) == null ? null : clean(paymentMethod).toUpperCase();
        this.receiptNumber = clean(receiptNumber);
    }

    // ---------- Identifiable ----------
    @Override
    public String getId() {
        return paymentId;
    }

    @Override
    public String getDisplayName() {
        return "Receipt " + receiptNumber;
    }

    @Override
    public boolean isActive() {
        return true;   // a payment has no status field, so it is always active
    }

    // ---------- Registrable ----------
    @Override
    public void validate() throws ValidationException {
        if (paymentId == null || paymentId.length() < MIN_ID_LENGTH
                || paymentId.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Payment ID must be " + MIN_ID_LENGTH
                    + "-" + MAX_ID_LENGTH + " characters.");
        }
        if (student == null) {
            throw new ValidationException("A student must be selected.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than zero.");
        }
        if (paymentDate == null || paymentDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Payment date is required and cannot be in the future.");
        }
        if (paymentMethod == null || !ALLOWED_METHODS.contains(paymentMethod)) {
            throw new ValidationException("Payment method must be one of: " + ALLOWED_METHODS);
        }
        if (receiptNumber == null || receiptNumber.isEmpty()) {
            throw new ValidationException("Receipt number cannot be empty.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("Payment ID    : " + paymentId);
        System.out.println("Student       : " + (student == null ? "None"
                : student.getRegistrationSummary()));
        System.out.println("Amount        : " + amount);
        System.out.println("Date          : " + paymentDate);
        System.out.println("Method        : " + paymentMethod);
        System.out.println("Receipt no.   : " + receiptNumber);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + paymentId + "] " + (student == null ? "?" : student.getDisplayName())
                + " paid " + amount + " via " + paymentMethod + " (receipt " + receiptNumber + ")";
    }

    // ---------- Helper ----------
    private static String clean(String value) {
        return value == null ? null : value.trim();
    }

    // ---------- Getters and setters ----------
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = clean(paymentId); }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = clean(paymentMethod) == null ? null : clean(paymentMethod).toUpperCase();
    }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = clean(receiptNumber); }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}