package com.auca.main.Main;

import com.auca.abstracts.AcademicMember;
import com.auca.abstracts.Person;
import com.auca.administrator.Administrator;
import com.auca.assessment.Assessment;
import com.auca.bookloan.BookLoan;
import com.auca.classroom.Classroom;
import com.auca.course.Course;
import com.auca.department.Department;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.BookUnavailableException;
import com.auca.exceptions.DuplicateRecordException;
import com.auca.exceptions.PrerequisiteMissingException;
import com.auca.exceptions.RecordNotFoundException;
import com.auca.exceptions.ValidationException;
import com.auca.feepayment.FeePayment;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.teacher.Teacher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Department> departments = new ArrayList<>();
    private static final List<Student> students = new ArrayList<>();
    private static final List<Teacher> teachers = new ArrayList<>();
    private static final List<Administrator> administrators = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();
    private static final List<Classroom> classrooms = new ArrayList<>();
    private static final List<Enrollment> enrollments = new ArrayList<>();
    private static final List<Assessment> assessments = new ArrayList<>();
    private static final List<FeePayment> payments = new ArrayList<>();
    private static final List<LibraryBook> books = new ArrayList<>();
    private static final List<BookLoan> loans = new ArrayList<>();

    // Allowed choices
    private static final List<String> STATUSES = List.of("ACTIVE", "INACTIVE");
    private static final List<String> YES_NO = List.of("YES", "NO");
    private static final List<String> EMPLOYMENT_TYPES = List.of("FULL_TIME", "PART_TIME", "CONTRACT");

    // Limits (must match Student.java and Administrator.java)
    private static final int MAX_STUDY_YEAR = 6;
    private static final int MAX_ACCESS_LEVEL = 5;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static Registrable beingEdited = null;
    private static class CancelledException extends RuntimeException {
        CancelledException() {
            super("Cancelled by the user.");
        }
    }
    private static class Field {
        String label;            
        String format;           
        String rule;           
        int min;                
        int max;                 
        String uniqueKey = "";  
        List<String> options;   
        String pickWhat;          
        Object value;            

        Field(String label, String format, String rule) {
            this.label = label;
            this.format = format;
            this.rule = rule;
        }
    }

    private static Field textField(String label, String format, int min, int max) {
        Field f = new Field(label, format, "text");
        f.min = min;
        f.max = max;
        return f;
    }

    private static Field numberField(String label, String format, int min, int max) {
        Field f = new Field(label, format, "number");
        f.min = min;
        f.max = max;
        return f;
    }

    private static Field ruleField(String label, String format, String rule) {
        return new Field(label, format, rule);
    }

    private static Field choiceField(String label, List<String> options) {
        Field f = new Field(label, choiceHint(options), "choice");
        f.options = options;
        return f;
    }

    private static Field pickField(String label, String format, String pickWhat) {
        Field f = new Field(label, format, "pick");
        f.pickWhat = pickWhat;
        return f;
    }

    private static Field fixedField(String label, String format, Object value) {
        Field f = new Field(label, format, "fixed");
        f.value = value;
        return f;
    }

    private static Field unique(Field f, String key) {
        f.uniqueKey = key;
        return f;
    }

    private static Field idField(String label) {
        return unique(textField(label, "[String | unique | 4-30 chars]", 4, 30), "id");
    }

    private static Field nameField(String label) {
        return textField(label, "[String | 2-60 chars]", 2, 60);
    }
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            String line = readLine("Select an option: ");

            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a number between 0 and 19.");
                continue;
            }

            try {
                if (choice == 0) {
                    running = false;
                    System.out.println("Goodbye!");
                } else {
                    route(choice);
                }
            } catch (CancelledException e) {
                System.out.println("Action cancelled. Back to the main menu.");
            } catch (ValidationException | DuplicateRecordException | RecordNotFoundException
                     | PrerequisiteMissingException | BookUnavailableException e) {
                System.out.println("! " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("! Unexpected problem: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        String[] left = {"1. Register Department", "2. Register Student", "3. Register Teacher",
                "4. Register Administrator", "5. Register Course", "6. Register Classroom",
                "7. Enroll Student", "8. Record Assessment", "9. Record Fee Payment",
                "10. Register Library Book"};
        String[] right = {"11. Borrow Library Book", "12. Return Library Book",
                "13. View All Records", "14. Search Record by ID", "15. Update Existing Record",
                "16. Student Academic Summary", "17. Student Payment History",
                "18. View Available Books", "19. Demonstrate Polymorphism", "0. Exit"};

        System.out.println();
        System.out.println("============== AUCA_ID SCHOOL MANAGEMENT ==============");
        for (int i = 0; i < left.length; i++) {
            System.out.printf(" %-30s %s%n", left[i], right[i]);
        }
    }

    private static void route(int choice) throws ValidationException, DuplicateRecordException,
            RecordNotFoundException, PrerequisiteMissingException, BookUnavailableException {
        switch (choice) {
            case 1: menuRegister("Department"); break;
            case 2: menuRegister("Student"); break;
            case 3: menuRegister("Teacher"); break;
            case 4: menuRegister("Administrator"); break;
            case 5: menuRegister("Course"); break;
            case 6: menuRegister("Classroom"); break;
            case 7: menuRegister("Enrollment"); break;
            case 8: menuRegister("Assessment"); break;
            case 9: menuRegister("Fee Payment"); break;
            case 10: menuRegister("Library Book"); break;
            case 11: menuRegister("Book Loan"); break;
            case 12: returnBook(); break;
            case 13: viewAllRecords(); break;
            case 14: searchById(); break;
            case 15: updateRecord(); break;
            case 16: studentSummary(); break;
            case 17: paymentHistory(); break;
            case 18: showAvailableBooks(); break;
            case 19: demonstratePolymorphism(); break;
            default: System.out.println("! Invalid option. Please choose a number from 0 to 19.");
        }
    }


    private static void menuRegister(String type) {
        while (true) {
            Registrable saved = registerNew(type);
            if (saved == null) {
                return;   
            }
            System.out.println("  [1] Register another   [2] View record   [0] Main menu");
            String option = readLine("Select option: ");
            if (option.equals("1")) {
                continue;
            }
            if (option.equals("2")) {
                saved.printDetails();
            }
            return;
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            System.out.println("\nInput closed. Goodbye.");
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }

    private static Registrable runForm(String title, String type, List<Field> fields,
                                       boolean startAtReview) {
        try {
            if (!startAtReview) {
                int total = fields.size();
                int perStep = (total + 2) / 3;
                int i = 0;
                int shownStep = -1;

                while (i < total) {
                    int step = i / perStep;
                    if (step != shownStep) {
                        printHeader(title, step + 1);
                        shownStep = step;
                    }
                    boolean forward = askField(fields.get(i), i + 1);
                    if (forward) {
                        i++;
                    } else if (i > 0) {
                        i--;
                    }
                }
            }
            return review(title, type, fields);

        } catch (CancelledException e) {
            System.out.println("Cancelled. Nothing was saved.");
            return null;
        } catch (PrerequisiteMissingException e) {
            System.out.println("! " + e.getMessage());
            System.out.println("  Cancelled. Nothing was saved.");
            return null;
        }
    }

    private static void printHeader(String title, int step) {
        System.out.println();
        System.out.println("+------------------------------------------------------------+");
        System.out.println("| AUCA_ID / SCHOOL MANAGEMENT");
        System.out.printf("| %-44s Step %d of 4%n", title, step);
        System.out.println("+------------------------------------------------------------+");
        System.out.println("| Type BACK to go to the previous field, CANCEL to discard.");
        System.out.println("+------------------------------------------------------------+");
    }

    private static boolean askField(Field field, int number) throws PrerequisiteMissingException {
       
        if (field.rule.equals("fixed")) {
            return true;
        }

       
        if (field.rule.equals("pick")) {
            System.out.println(String.format("%02d %s %s", number, field.label, field.format));
            while (true) {
                try {
                    field.value = pickRecord(field.pickWhat);
                    return true;
                } catch (BookUnavailableException e) {
                    System.out.println("! " + e.getMessage());
                    System.out.println("  Please choose another book.");
                }
            }
        }

       
        while (true) {
            String text = readLine(String.format("%02d %s %s : ", number, field.label, field.format));
            if (text.equalsIgnoreCase("CANCEL")) {
                throw new CancelledException();
            }
            if (text.equalsIgnoreCase("BACK")) {
                return false;
            }
            try {
                field.value = checkValue(field, text);
                return true;
            } catch (ValidationException | DuplicateRecordException e) {
                System.out.println("! " + e.getMessage());
                System.out.println("  Please re-enter field " + String.format("%02d", number) + ".");
            }
        }
    }

   
    private static Registrable review(String title, String type, List<Field> fields)
            throws PrerequisiteMissingException {
        while (true) {
            System.out.println();
            System.out.println("+---------------- REVIEW: " + title + " ----------------+");
            for (int i = 0; i < fields.size(); i++) {
                Field f = fields.get(i);
                System.out.printf("| %02d %-20s: %s%n", i + 1, f.label, display(f.value));
            }
            System.out.println("+------------------------------------------------------------+");
            System.out.println("| [1] Save   [2] Edit field   [0] Cancel");

            String option = readLine("Select option: ");
            if (option.equals("1")) {
                try {
                    recheckUnique(fields);
                    Object[] values = new Object[fields.size()];
                    for (int i = 0; i < values.length; i++) {
                        values[i] = fields.get(i).value;
                    }
                    Registrable record = build(type, values);   
                    record.validate();                          
                    return record;
                } catch (ValidationException | DuplicateRecordException | BookUnavailableException e) {
                    System.out.println("! " + e.getMessage());
                    System.out.println("  Choose 2 to fix the problem, or 0 to cancel.");
                }
            } else if (option.equals("2")) {
                editField(fields);
            } else if (option.equals("0")) {
                throw new CancelledException();
            } else {
                System.out.println("! Please choose 1, 2 or 0.");
            }
        }
    }

    private static void editField(List<Field> fields) throws PrerequisiteMissingException {
        String text = readLine("Field number to edit (1-" + fields.size() + "): ");
        int index;
        try {
            index = Integer.parseInt(text) - 1;
        } catch (NumberFormatException e) {
            System.out.println("! Please enter a field number.");
            return;
        }
        if (index < 0 || index >= fields.size()) {
            System.out.println("! There is no field with that number.");
            return;
        }
        Field field = fields.get(index);
        if (field.rule.equals("fixed")) {
            System.out.println("! This field is filled automatically and cannot be edited.");
            return;
        }
        System.out.println("Old value: " + display(field.value));
        askField(field, index + 1);
    }

   
    private static void recheckUnique(List<Field> fields) throws DuplicateRecordException {
        for (Field f : fields) {
            if (!f.uniqueKey.isEmpty() && isTaken(f.uniqueKey, (String) f.value)) {
                throw new DuplicateRecordException(f.label + " " + f.value + " already exists.");
            }
        }
    }

    private static String display(Object value) {
        if (value == null) {
            return "(none)";
        }
        if (value instanceof Registrable) {
            return ((Registrable) value).getRegistrationSummary();
        }
        return value.toString();
    }

   
    private static Object pickRecord(String what)
            throws PrerequisiteMissingException, BookUnavailableException {
        List<Registrable> items = new ArrayList<>();
        String type;
        boolean optional = false;

        switch (what) {
            case "department": items.addAll(departments); type = "Department"; break;
            case "teacher": items.addAll(teachers); type = "Teacher"; break;
            case "optionalTeacher": items.addAll(teachers); type = "Teacher"; optional = true; break;
            case "student": items.addAll(students); type = "Student"; break;
            case "course": items.addAll(courses); type = "Course"; break;
            case "book": items.addAll(books); type = "Library Book"; break;
            case "activeEnrollment":
                for (Enrollment e : enrollments) {
                    if (e.isActive()) {
                        items.add(e);
                    }
                }
                type = "Enrollment";
                break;
            default:
                throw new IllegalArgumentException("Unknown selection: " + what);
        }

        while (true) {
            if (items.isEmpty() && optional) {
                System.out.println("  No " + type + " is registered yet. You can assign one later.");
                return null;
            }

            System.out.println("+----------------- CHOOSE " + type.toUpperCase() + " -----------------+");
            if (items.isEmpty()) {
                System.out.println("| No " + type + " is registered yet.");
            }
            for (int i = 0; i < items.size(); i++) {
                System.out.println("| " + (i + 1) + " " + items.get(i).getRegistrationSummary());
            }
            int registerOption = items.size() + 1;
            if (!optional) {
                System.out.println("| " + registerOption + " [+] Register a new " + type);
            }
            System.out.println("| 0 " + (optional ? "Skip (none for now)" : "Cancel"));

            String text = readLine("Select option: ");
            int n;
            try {
                n = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a number.");
                continue;
            }

            if (n == 0) {
                if (optional) {
                    return null;
                }
                throw new CancelledException();
            }

            Registrable chosen;
            if (n >= 1 && n <= items.size()) {
                chosen = items.get(n - 1);
            } else if (!optional && n == registerOption) {
                chosen = registerNew(type);   
                if (chosen == null) {
                    if (items.isEmpty()) {
                        throw new PrerequisiteMissingException("No " + type + " exists. Register a "
                                + type + " first.");
                    }
                    continue;
                }
            } else {
                System.out.println("! Please choose a number from the list.");
                continue;
            }

      
            if (what.equals("book") && ((LibraryBook) chosen).getAvailableCopies() <= 0) {
                throw new BookUnavailableException("No copy of '"
                        + ((LibraryBook) chosen).getTitle() + "' is available right now.");
            }
            if (what.equals("activeEnrollment") && !((Enrollment) chosen).isActive()) {
                System.out.println("! That enrollment is not ACTIVE, so it cannot be assessed.");
                continue;
            }
            return chosen;
        }
    }

    
    private static Object checkValue(Field f, String text)
            throws ValidationException, DuplicateRecordException {
        if (text.isEmpty()) {
            throw new ValidationException("This field cannot be blank.");
        }

        Object value;
        switch (f.rule) {
            case "text":
                if (text.length() < f.min || text.length() > f.max) {
                    throw new ValidationException("Must be " + f.min + "-" + f.max + " characters.");
                }
                value = text;
                break;

            case "email":
                if (text.length() > Registrable.MAX_EMAIL_LENGTH || !text.matches(EMAIL_PATTERN)) {
                    throw new ValidationException("Invalid email (example: name@example.com, maximum "
                            + Registrable.MAX_EMAIL_LENGTH + " characters).");
                }
                value = text;
                break;

            case "phone":
                if (!text.matches("^\\+?\\d{10,15}$")) {
                    throw new ValidationException("Phone must have 10-15 digits, optional leading +.");
                }
                value = text;
                break;

            case "isbn":
                if (!text.replace("-", "").matches("\\d{10}|\\d{13}")) {
                    throw new ValidationException("ISBN must have 10 or 13 digits (hyphens allowed).");
                }
                value = text;
                break;

            case "pastDate": {
                LocalDate date = readDate(text);
                if (!date.isBefore(LocalDate.now())) {
                    throw new ValidationException("Date must be in the past.");
                }
                value = date;
                break;
            }

            case "notFutureDate": {
                LocalDate date = readDate(text);
                if (date.isAfter(LocalDate.now())) {
                    throw new ValidationException("Date cannot be in the future.");
                }
                value = date;
                break;
            }

            case "anyDate":
                value = readDate(text);
                break;

            case "number": {
                try {
                    int number = Integer.parseInt(text);
                    if (number < f.min || number > f.max) {
                        throw new ValidationException("Must be a whole number from "
                                + f.min + " to " + f.max + ".");
                    }
                    value = number;
                } catch (NumberFormatException e) {
                    throw new ValidationException("Please enter a whole number.");
                }
                break;
            }

            case "money": {
                try {
                    BigDecimal amount = new BigDecimal(text);
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new ValidationException("Amount must be greater than zero.");
                    }
                    value = amount;
                } catch (NumberFormatException e) {
                    throw new ValidationException("Please enter a valid amount (example: 450000.50).");
                }
                break;
            }

            case "positiveNumber":
            case "zeroOrMoreNumber": {
                try {
                    double number = Double.parseDouble(text);
                    if (Double.isNaN(number) || Double.isInfinite(number)) {
                        throw new NumberFormatException();
                    }
                    if (f.rule.equals("positiveNumber") && number <= 0) {
                        throw new ValidationException("Must be greater than zero.");
                    }
                    if (number < 0) {
                        throw new ValidationException("Cannot be negative.");
                    }
                    value = number;
                } catch (NumberFormatException e) {
                    throw new ValidationException("Please enter a valid number (example: 85.5).");
                }
                break;
            }

            case "choice": {
                String found = null;
                for (int i = 0; i < f.options.size(); i++) {
                    if (text.equals(String.valueOf(i + 1)) || text.equalsIgnoreCase(f.options.get(i))) {
                        found = f.options.get(i);
                    }
                }
                if (found == null) {
                    throw new ValidationException("Please choose one of: " + f.format);
                }
                value = found;
                break;
            }

            default:
                throw new ValidationException("Unknown rule: " + f.rule);
        }

        // Duplicate check
        if (!f.uniqueKey.isEmpty() && isTaken(f.uniqueKey, text)) {
            throw new DuplicateRecordException(f.label + " " + text + " already exists.");
        }
        return value;
    }

    private static LocalDate readDate(String text) throws ValidationException {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid date: use YYYY-MM-DD (example: 2004-05-16).");
        }
    }


    private static String choiceHint(List<String> options) {
        String hint = "[";
        for (int i = 0; i < options.size(); i++) {
            if (i > 0) {
                hint = hint + " / ";
            }
            hint = hint + (i + 1) + " " + options.get(i);
        }
        return hint + "]";
    }

    private static List<Identifiable> allRecords() {
        List<Identifiable> all = new ArrayList<>();
        all.addAll(departments);
        all.addAll(students);
        all.addAll(teachers);
        all.addAll(administrators);
        all.addAll(courses);
        all.addAll(classrooms);
        all.addAll(enrollments);
        all.addAll(assessments);
        all.addAll(payments);
        all.addAll(books);
        all.addAll(loans);
        return all;
    }

    private static List<Person> allPeople() {
        List<Person> all = new ArrayList<>();
        all.addAll(students);
        all.addAll(teachers);
        all.addAll(administrators);
        return all;
    }

    private static List<AcademicMember> allMembers() {
        List<AcademicMember> all = new ArrayList<>();
        all.addAll(students);
        all.addAll(teachers);
        return all;
    }

    // Is this value already used? (the record being edited is ignored)
    private static boolean isTaken(String key, String text) {
        switch (key) {
            case "id":
                for (Identifiable r : allRecords()) {
                    if (r != beingEdited && r.getId().equalsIgnoreCase(text)) {
                        return true;
                    }
                }
                return false;
            case "deptCode":
                for (Department d : departments) {
                    if (d != beingEdited && d.getDepartmentCode().equalsIgnoreCase(text)) {
                        return true;
                    }
                }
                return false;
            case "email":
                for (Person p : allPeople()) {
                    if (p != beingEdited && p.getEmail().equalsIgnoreCase(text)) {
                        return true;
                    }
                }
                return false;
            case "instEmail":
                for (AcademicMember m : allMembers()) {
                    if (m != beingEdited && m.getInstitutionalEmail().equalsIgnoreCase(text)) {
                        return true;
                    }
                }
                return false;
            case "memberNumber":
                for (AcademicMember m : allMembers()) {
                    if (m != beingEdited && m.getMemberNumber().equalsIgnoreCase(text)) {
                        return true;
                    }
                }
                return false;
            case "studentNumber":
                for (Student s : students) {
                    if (s != beingEdited && s.getStudentNumber().equalsIgnoreCase(text)) {
                        return true;
                    }
                }
                return false;
            case "employeeNumber":
                for (Teacher t : teachers) {
                    if (t != beingEdited && t.getEmployeeNumber().equalsIgnoreCase(text)) {
                        return true;
                    }
                }
                return false;
            case "staffNumber":
                for (Administrator a : administrators) {
                    if (a != beingEdited && a.getStaffNumber().equalsIgnoreCase(text)) {
                        return true;
                    }
                }
                return false;
            case "courseCode":
                for (Course c : courses) {
                    if (c != beingEdited && c.getCourseCode().equalsIgnoreCase(text)) {
                        return true;
                    }
                }
                return false;
            case "receipt":
                for (FeePayment p : payments) {
                    if (p != beingEdited && p.getReceiptNumber().equalsIgnoreCase(text)) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }


    private static void addPersonFields(List<Field> f) {
        f.add(idField("Person ID"));
        f.add(nameField("First name"));
        f.add(nameField("Last name"));
        f.add(unique(ruleField("Email", "[String | unique | email]", "email"), "email"));
        f.add(ruleField("Phone", "[String | 10-15 digits]", "phone"));
        f.add(ruleField("Date of birth", "[LocalDate | YYYY-MM-DD]", "pastDate"));
    }

    private static void addMemberFields(List<Field> f) {
        f.add(unique(textField("Member number", "[String | unique | 4-30]", 4, 30), "memberNumber"));
        f.add(pickField("Department", "[Department | select]", "department"));
        f.add(ruleField("Join date", "[LocalDate | YYYY-MM-DD]", "notFutureDate"));
        f.add(choiceField("Academic status", STATUSES));
        f.add(unique(ruleField("Institutional email", "[String | unique | email]", "email"), "instEmail"));
        f.add(textField("Advisor name", "[String | 2-120 chars]", 2, 120));
    }

    // The list of questions for each type of record
    private static List<Field> fieldsFor(String type) {
        List<Field> f = new ArrayList<>();
        switch (type) {
            case "Department":
                f.add(idField("Department ID"));
                f.add(nameField("Department name"));
                f.add(unique(textField("Department code", "[String | unique | 2-10 chars]", 2, 10), "deptCode"));
                f.add(textField("Office location", "[String | 2-60 chars]", 2, 60));
                f.add(ruleField("Contact email", "[String | email]", "email"));
                f.add(pickField("Head teacher", "[Teacher | optional]", "optionalTeacher"));
                break;

            case "Student":
                addPersonFields(f);
                addMemberFields(f);
                f.add(unique(textField("Student number", "[String | unique | 4-30]", 4, 30), "studentNumber"));
                f.add(textField("Program name", "[String | 2-120 chars]", 2, 120));
                f.add(ruleField("Admission date", "[LocalDate | YYYY-MM-DD]", "notFutureDate"));
                f.add(numberField("Study year", "[int | 1-" + MAX_STUDY_YEAR + "]", 1, MAX_STUDY_YEAR));
                f.add(ruleField("Guardian phone", "[String | 10-15 digits]", "phone"));
                f.add(choiceField("Scholarship", YES_NO));
                break;

            case "Teacher":
                addPersonFields(f);
                addMemberFields(f);
                f.add(unique(textField("Employee number", "[String | unique | 4-30]", 4, 30), "employeeNumber"));
                f.add(textField("Specialization", "[String | 2-120 chars]", 2, 120));
                f.add(ruleField("Hire date", "[LocalDate | YYYY-MM-DD]", "notFutureDate"));
                f.add(ruleField("Salary", "[BigDecimal | greater than 0]", "money"));
                f.add(textField("Office number", "[String | 1-30 chars]", 1, 30));
                f.add(choiceField("Employment type", EMPLOYMENT_TYPES));
                break;

            case "Administrator":
                addPersonFields(f);
                f.add(unique(textField("Staff number", "[String | unique | 4-30]", 4, 30), "staffNumber"));
                f.add(textField("Job title", "[String | 2-120 chars]", 2, 120));
                f.add(textField("Assigned office", "[String | 1-60 chars]", 1, 60));
                f.add(ruleField("Employment date", "[LocalDate | YYYY-MM-DD]", "notFutureDate"));
                f.add(numberField("Access level", "[int | 1-" + MAX_ACCESS_LEVEL + "]", 1, MAX_ACCESS_LEVEL));
                f.add(choiceField("Active", YES_NO));
                break;

            case "Course":
                f.add(idField("Course ID"));
                f.add(unique(textField("Course code", "[String | unique | 4-30]", 4, 30), "courseCode"));
                f.add(textField("Course title", "[String | 2-120 chars]", 2, 120));
                f.add(numberField("Credit hours", "[int | 1-6]", 1, 6));
                f.add(pickField("Department", "[Department | select]", "department"));
                f.add(pickField("Assigned teacher", "[Teacher | select]", "teacher"));
                break;

            case "Classroom":
                f.add(idField("Classroom ID"));
                f.add(nameField("Building name"));
                f.add(textField("Room number", "[String | 1-20 chars]", 1, 20));
                f.add(numberField("Capacity", "[int | 1-100000]", 1, 100000));
                f.add(numberField("Floor number", "[int | 0-200]", 0, 200));
                f.add(choiceField("Available", YES_NO));
                break;

            case "Enrollment":
                f.add(idField("Enrollment ID"));
                f.add(pickField("Student", "[Student | select]", "student"));
                f.add(pickField("Course", "[Course | select]", "course"));
                f.add(ruleField("Enrollment date", "[LocalDate | YYYY-MM-DD]", "notFutureDate"));
                f.add(choiceField("Semester", Enrollment.ALLOWED_SEMESTERS));
                f.add(choiceField("Enrollment status", Enrollment.ALLOWED_STATUSES));
                break;

            case "Assessment":
                f.add(idField("Assessment ID"));
                f.add(pickField("Enrollment", "[active Enrollment | select]", "activeEnrollment"));
                f.add(choiceField("Assessment type", Assessment.ALLOWED_TYPES));
                f.add(ruleField("Maximum score", "[double | greater than 0]", "positiveNumber"));
                f.add(ruleField("Obtained score", "[double | 0 to maximum]", "zeroOrMoreNumber"));
                f.add(ruleField("Assessment date", "[LocalDate | YYYY-MM-DD]", "anyDate"));
                break;

            case "Fee Payment":
                f.add(idField("Payment ID"));
                f.add(pickField("Student", "[Student | select]", "student"));
                f.add(ruleField("Amount", "[BigDecimal | greater than 0]", "money"));
                f.add(ruleField("Payment date", "[LocalDate | YYYY-MM-DD]", "notFutureDate"));
                f.add(choiceField("Payment method", FeePayment.ALLOWED_METHODS));
                f.add(unique(textField("Receipt number", "[String | unique | 1-30]", 1, 30), "receipt"));
                break;

            case "Library Book":
                f.add(idField("Book ID"));
                f.add(ruleField("ISBN", "[String | 10 or 13 digits]", "isbn"));
                f.add(textField("Title", "[String | 2-120 chars]", 2, 120));
                f.add(textField("Author", "[String | 2-120 chars]", 2, 120));
                f.add(numberField("Total copies", "[int | 0 or more]", 0, 100000));
                f.add(numberField("Available copies", "[int | 0 to total]", 0, 100000));
                break;

            case "Book Loan":
                f.add(idField("Loan ID"));
                f.add(pickField("Student", "[Student | select]", "student"));
                f.add(pickField("Book", "[Library Book | select]", "book"));
                f.add(ruleField("Borrowed date", "[LocalDate | YYYY-MM-DD]", "notFutureDate"));
                f.add(ruleField("Due date", "[LocalDate | after borrowed date]", "anyDate"));
                f.add(fixedField("Returned date", "[LocalDate | empty until returned]", null));
                break;
        }
        return f;
    }


    private static Registrable build(String type, Object[] v)
            throws ValidationException, DuplicateRecordException, BookUnavailableException {
        switch (type) {
            case "Department":
                return new Department((String) v[0], (String) v[1], (String) v[2],
                        (String) v[3], (String) v[4], (Teacher) v[5]);

            case "Student":
                return new Student((String) v[0], (String) v[1], (String) v[2],
                        (String) v[3], (String) v[4], (LocalDate) v[5],
                        (String) v[6], (Department) v[7], (LocalDate) v[8],
                        (String) v[9], (String) v[10], (String) v[11],
                        (String) v[12], (String) v[13], (LocalDate) v[14],
                        (Integer) v[15], (String) v[16], "YES".equals(v[17]));

            case "Teacher":
                return new Teacher((String) v[0], (String) v[1], (String) v[2],
                        (String) v[3], (String) v[4], (LocalDate) v[5],
                        (String) v[6], (Department) v[7], (LocalDate) v[8],
                        (String) v[9], (String) v[10], (String) v[11],
                        (String) v[12], (String) v[13], (LocalDate) v[14],
                        (BigDecimal) v[15], (String) v[16], (String) v[17]);

            case "Administrator":
                return new Administrator((String) v[0], (String) v[1], (String) v[2],
                        (String) v[3], (String) v[4], (LocalDate) v[5],
                        (String) v[6], (String) v[7], (String) v[8],
                        (LocalDate) v[9], (Integer) v[10], "YES".equals(v[11]));

            case "Course":
                return new Course((String) v[0], (String) v[1], (String) v[2],
                        (Integer) v[3], (Department) v[4], (Teacher) v[5]);

            case "Classroom":
                return new Classroom((String) v[0], (String) v[1], (String) v[2],
                        (Integer) v[3], (Integer) v[4], "YES".equals(v[5]));

            case "Enrollment": {
                Enrollment e = new Enrollment((String) v[0], (Student) v[1], (Course) v[2],
                        (LocalDate) v[3], (String) v[4], (String) v[5]);

                for (Enrollment old : enrollments) {
                    if (old != beingEdited && old.isActive() && e.isActive()
                            && old.getStudent() == e.getStudent()
                            && old.getCourse() == e.getCourse()
                            && old.getSemester().equals(e.getSemester())) {
                        throw new DuplicateRecordException("This student is already actively enrolled in "
                                + "this course for " + e.getSemester() + ".");
                    }
                }
                return e;
            }

            case "Assessment":
                return new Assessment((String) v[0], (Enrollment) v[1], (String) v[2],
                        (Double) v[3], (Double) v[4], (LocalDate) v[5]);

            case "Fee Payment":
                return new FeePayment((String) v[0], (Student) v[1], (BigDecimal) v[2],
                        (LocalDate) v[3], (String) v[4], (String) v[5]);

            case "Library Book":
                return new LibraryBook((String) v[0], (String) v[1], (String) v[2],
                        (String) v[3], (Integer) v[4], (Integer) v[5]);

            case "Book Loan": {
                BookLoan loan = new BookLoan((String) v[0], (Student) v[1], (LibraryBook) v[2],
                        (LocalDate) v[3], (LocalDate) v[4], (LocalDate) v[5]);
                if (loan.getBook().getAvailableCopies() <= 0) {
                    throw new BookUnavailableException("No copy of '" + loan.getBook().getTitle()
                            + "' is available.");
                }
                return loan;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + type);
    }


    private static void storeRecord(Registrable r) {
        if (r instanceof Department) {
            departments.add((Department) r);
        } else if (r instanceof Student) {
            students.add((Student) r);
        } else if (r instanceof Teacher) {
            teachers.add((Teacher) r);
        } else if (r instanceof Administrator) {
            administrators.add((Administrator) r);
        } else if (r instanceof Course) {
            courses.add((Course) r);
        } else if (r instanceof Classroom) {
            classrooms.add((Classroom) r);
        } else if (r instanceof Enrollment) {
            enrollments.add((Enrollment) r);
        } else if (r instanceof Assessment) {
            assessments.add((Assessment) r);
        } else if (r instanceof FeePayment) {
            payments.add((FeePayment) r);
        } else if (r instanceof LibraryBook) {
            books.add((LibraryBook) r);
        } else if (r instanceof BookLoan) {
            loans.add((BookLoan) r);
        }
    }


    private static void removeRecord(Registrable r) {
        departments.remove(r);
        students.remove(r);
        teachers.remove(r);
        administrators.remove(r);
        courses.remove(r);
        classrooms.remove(r);
        enrollments.remove(r);
        assessments.remove(r);
        payments.remove(r);
        books.remove(r);
        loans.remove(r);
    }


    private static Registrable registerNew(String type) {
        Registrable record = runForm(type.toUpperCase() + " REGISTRATION", type, fieldsFor(type), false);
        if (record == null) {
            return null;
        }
        storeRecord(record);


        if (record instanceof BookLoan) {
            LibraryBook book = ((BookLoan) record).getBook();
            book.setAvailableCopies(book.getAvailableCopies() - 1);
        }
        System.out.println("+ " + type + " registered successfully: " + record.getRegistrationSummary());
        return record;
    }



    private static void returnBook() {
        List<BookLoan> openLoans = new ArrayList<>();
        for (BookLoan loan : loans) {
            if (loan.isActive()) {          // active = not returned yet
                openLoans.add(loan);
            }
        }
        if (openLoans.isEmpty()) {
            System.out.println("No book is currently borrowed. Nothing to return.");
            return;
        }

        while (true) {
            System.out.println("+----------------- BOOKS TO RETURN -----------------+");
            for (int i = 0; i < openLoans.size(); i++) {
                System.out.println("| " + (i + 1) + " " + openLoans.get(i).getRegistrationSummary());
            }
            System.out.println("| 0 Cancel");
            String text = readLine("Select option: ");

            int n;
            try {
                n = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a number.");
                continue;
            }
            if (n == 0) {
                System.out.println("Return cancelled. Nothing was changed.");
                return;
            }
            if (n < 1 || n > openLoans.size()) {
                System.out.println("! Please choose a number from the list.");
                continue;
            }

            BookLoan loan = openLoans.get(n - 1);
            loan.printDetails();
            String confirm = readLine("Return this book today? [1 Yes / 0 No]: ");
            if (!confirm.equals("1")) {
                System.out.println("Return cancelled. Nothing was changed.");
                return;
            }

            loan.setReturnedDate(LocalDate.now());
            try {
                loan.validate();
            } catch (ValidationException e) {
                loan.setReturnedDate(null);   // undo, nothing changes
                System.out.println("! " + e.getMessage());
                return;
            }
            LibraryBook book = loan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);   // exactly once
            System.out.println("+ Book returned. Available copies of '" + book.getTitle() + "': "
                    + book.getAvailableCopies() + "/" + book.getTotalCopies());
            return;
        }
    }


    private static void viewAllRecords() {
        printList("DEPARTMENTS", departments);
        printList("STUDENTS", students);
        printList("TEACHERS", teachers);
        printList("ADMINISTRATORS", administrators);
        printList("COURSES", courses);
        printList("CLASSROOMS", classrooms);
        printList("ENROLLMENTS", enrollments);
        printList("ASSESSMENTS", assessments);
        printList("FEE PAYMENTS", payments);
        printList("LIBRARY BOOKS", books);
        printList("BOOK LOANS", loans);
        if (allRecords().isEmpty()) {
            System.out.println("No records have been saved yet.");
        }
        readLine("Press ENTER to return to the menu...");
    }


    private static void printList(String title, List<? extends Registrable> list) {
        System.out.println("--- " + title + " (" + list.size() + ") ---");
        if (list.isEmpty()) {
            System.out.println("   (none)");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + list.get(i).getRegistrationSummary());
        }
    }


    private static void searchById() throws RecordNotFoundException {
        String id = readLine("Enter the ID to search (0 to cancel): ");
        if (id.equals("0")) {
            return;
        }
        Identifiable found = findById(id);
        System.out.println("Found a " + typeOf((Registrable) found) + ":");
        ((Registrable) found).printDetails();
    }

    private static Identifiable findById(String id) throws RecordNotFoundException {
        for (Identifiable record : allRecords()) {
            if (record.getId().equalsIgnoreCase(id)) {
                return record;
            }
        }
        throw new RecordNotFoundException("No record with ID " + id + " was found.");
    }


    private static void updateRecord() throws RecordNotFoundException {
        String id = readLine("Enter the ID of the record to update (0 to cancel): ");
        if (id.equals("0")) {
            return;
        }
        Registrable old = (Registrable) findById(id);
        String type = typeOf(old);

        if (type.equals("Book Loan")) {
            System.out.println("! Book loans change only through Borrow and Return.");
            return;
        }


        List<Field> fields = fieldsFor(type);
        Object[] current = valuesOf(old);
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).value = current[i];
        }

        Registrable updated = null;
        beingEdited = old;
        try {
            updated = runForm("UPDATE " + type.toUpperCase(), type, fields, true);
        } finally {
            beingEdited = null;
        }
        if (updated == null) {
            System.out.println("Nothing was changed.");
            return;
        }

        String confirm = readLine("Apply these changes? [1 Yes / 0 No]: ");
        if (!confirm.equals("1")) {
            System.out.println("Nothing was changed.");
            return;
        }
        removeRecord(old);
        storeRecord(updated);
        replaceReferences(old, updated);
        System.out.println("+ Record updated: " + updated.getRegistrationSummary());
    }


    private static String typeOf(Registrable r) {
        if (r instanceof Department) return "Department";
        if (r instanceof Student) return "Student";
        if (r instanceof Teacher) return "Teacher";
        if (r instanceof Administrator) return "Administrator";
        if (r instanceof Course) return "Course";
        if (r instanceof Classroom) return "Classroom";
        if (r instanceof Enrollment) return "Enrollment";
        if (r instanceof Assessment) return "Assessment";
        if (r instanceof FeePayment) return "Fee Payment";
        if (r instanceof LibraryBook) return "Library Book";
        return "Book Loan";
    }


    private static Object[] valuesOf(Registrable r) {
        List<Object> v = new ArrayList<>();

        if (r instanceof Person) {
            Person p = (Person) r;
            v.add(p.getPersonId());
            v.add(p.getFirstName());
            v.add(p.getLastName());
            v.add(p.getEmail());
            v.add(p.getPhoneNumber());
            v.add(p.getDateOfBirth());
        }
        if (r instanceof AcademicMember) {
            AcademicMember m = (AcademicMember) r;
            v.add(m.getMemberNumber());
            v.add(m.getDepartment());
            v.add(m.getJoinDate());
            v.add(m.getAcademicStatus());
            v.add(m.getInstitutionalEmail());
            v.add(m.getAdvisorName());
        }

        if (r instanceof Student) {
            Student s = (Student) r;
            v.add(s.getStudentNumber());
            v.add(s.getProgramName());
            v.add(s.getAdmissionDate());
            v.add(s.getStudyYear());
            v.add(s.getGuardianPhone());
            v.add(s.isScholarshipStatus() ? "YES" : "NO");
        } else if (r instanceof Teacher) {
            Teacher t = (Teacher) r;
            v.add(t.getEmployeeNumber());
            v.add(t.getSpecialization());
            v.add(t.getHireDate());
            v.add(t.getSalary());
            v.add(t.getOfficeNumber());
            v.add(t.getEmploymentType());
        } else if (r instanceof Administrator) {
            Administrator a = (Administrator) r;
            v.add(a.getStaffNumber());
            v.add(a.getJobTitle());
            v.add(a.getAssignedOffice());
            v.add(a.getEmploymentDate());
            v.add(a.getAccessLevel());
            v.add(a.isActive() ? "YES" : "NO");
        } else if (r instanceof Department) {
            Department d = (Department) r;
            v.add(d.getDepartmentId());
            v.add(d.getDepartmentName());
            v.add(d.getDepartmentCode());
            v.add(d.getOfficeLocation());
            v.add(d.getContactEmail());
            v.add(d.getHeadTeacher());
        } else if (r instanceof Course) {
            Course c = (Course) r;
            v.add(c.getCourseId());
            v.add(c.getCourseCode());
            v.add(c.getCourseTitle());
            v.add(c.getCreditHours());
            v.add(c.getDepartment());
            v.add(c.getAssignedTeacher());
        } else if (r instanceof Classroom) {
            Classroom c = (Classroom) r;
            v.add(c.getClassroomId());
            v.add(c.getBuildingName());
            v.add(c.getRoomNumber());
            v.add(c.getCapacity());
            v.add(c.getFloorNumber());
            v.add(c.isActive() ? "YES" : "NO");
        } else if (r instanceof Enrollment) {
            Enrollment e = (Enrollment) r;
            v.add(e.getEnrollmentId());
            v.add(e.getStudent());
            v.add(e.getCourse());
            v.add(e.getEnrollmentDate());
            v.add(e.getSemester());
            v.add(e.getEnrollmentStatus());
        } else if (r instanceof Assessment) {
            Assessment a = (Assessment) r;
            v.add(a.getAssessmentId());
            v.add(a.getEnrollment());
            v.add(a.getAssessmentType());
            v.add(a.getMaximumScore());
            v.add(a.getObtainedScore());
            v.add(a.getAssessmentDate());
        } else if (r instanceof FeePayment) {
            FeePayment p = (FeePayment) r;
            v.add(p.getPaymentId());
            v.add(p.getStudent());
            v.add(p.getAmount());
            v.add(p.getPaymentDate());
            v.add(p.getPaymentMethod());
            v.add(p.getReceiptNumber());
        } else if (r instanceof LibraryBook) {
            LibraryBook b = (LibraryBook) r;
            v.add(b.getBookId());
            v.add(b.getIsbn());
            v.add(b.getTitle());
            v.add(b.getAuthor());
            v.add(b.getTotalCopies());
            v.add(b.getAvailableCopies());
        }
        return v.toArray();
    }


    private static void replaceReferences(Registrable oldRecord, Registrable newRecord) {
        if (oldRecord instanceof Department) {
            for (Student s : students) {
                if (s.getDepartment() == oldRecord) s.setDepartment((Department) newRecord);
            }
            for (Teacher t : teachers) {
                if (t.getDepartment() == oldRecord) t.setDepartment((Department) newRecord);
            }
            for (Course c : courses) {
                if (c.getDepartment() == oldRecord) c.setDepartment((Department) newRecord);
            }
        }
        if (oldRecord instanceof Teacher) {
            for (Department d : departments) {
                if (d.getHeadTeacher() == oldRecord) d.setHeadTeacher((Teacher) newRecord);
            }
            for (Course c : courses) {
                if (c.getAssignedTeacher() == oldRecord) c.setAssignedTeacher((Teacher) newRecord);
            }
        }
        if (oldRecord instanceof Student) {
            for (Enrollment e : enrollments) {
                if (e.getStudent() == oldRecord) e.setStudent((Student) newRecord);
            }
            for (FeePayment p : payments) {
                if (p.getStudent() == oldRecord) p.setStudent((Student) newRecord);
            }
            for (BookLoan l : loans) {
                if (l.getStudent() == oldRecord) l.setStudent((Student) newRecord);
            }
        }
        if (oldRecord instanceof Course) {
            for (Enrollment e : enrollments) {
                if (e.getCourse() == oldRecord) e.setCourse((Course) newRecord);
            }
        }
        if (oldRecord instanceof Enrollment) {
            for (Assessment a : assessments) {
                if (a.getEnrollment() == oldRecord) a.setEnrollment((Enrollment) newRecord);
            }
        }
        if (oldRecord instanceof LibraryBook) {
            for (BookLoan l : loans) {
                if (l.getBook() == oldRecord) l.setBook((LibraryBook) newRecord);
            }
        }
    }


    private static void studentSummary() throws PrerequisiteMissingException {
        Student s = chooseStudent();
        if (s == null) {
            return;
        }
        System.out.println();
        System.out.println("========== ACADEMIC SUMMARY ==========");
        System.out.println("Student    : " + s.getDisplayName() + " (" + s.getStudentNumber() + ")");
        System.out.println("Program    : " + s.getProgramName() + ", year " + s.getStudyYear());
        System.out.println("Department : " + s.getDepartment().getDepartmentName());
        System.out.println("Status     : " + s.getAcademicStatus());
        System.out.println("--------------------------------------");

        boolean hasEnrollment = false;
        double allObtained = 0;
        double allMaximum = 0;

        for (Enrollment e : enrollments) {
            if (e.getStudent() != s) {
                continue;
            }
            hasEnrollment = true;
            System.out.println("Course: " + e.getCourse().getDisplayName()
                    + " | " + e.getSemester() + " | " + e.getEnrollmentStatus());

            double obtained = 0;
            double maximum = 0;
            int count = 0;
            for (Assessment a : assessments) {
                if (a.getEnrollment() != e) {
                    continue;
                }
                System.out.println("   " + a.getAssessmentType() + ": "
                        + a.getObtainedScore() + "/" + a.getMaximumScore());
                obtained += a.getObtainedScore();
                maximum += a.getMaximumScore();
                count++;
            }
            if (count == 0) {
                System.out.println("   No assessment recorded yet.");
            } else {
                System.out.printf("   Course total: %.1f/%.1f (%.1f%%)%n",
                        obtained, maximum, obtained / maximum * 100);
            }
            allObtained += obtained;
            allMaximum += maximum;
        }

        if (!hasEnrollment) {
            System.out.println("This student has no enrollment yet.");
        } else if (allMaximum > 0) {
            System.out.printf("OVERALL: %.1f/%.1f (%.1f%%)%n",
                    allObtained, allMaximum, allObtained / allMaximum * 100);
        }
    }


    private static Student chooseStudent() throws PrerequisiteMissingException {
        if (students.isEmpty()) {
            throw new PrerequisiteMissingException("No student exists. Register a student first.");
        }
        while (true) {
            System.out.println("+----------------- CHOOSE STUDENT -----------------+");
            for (int i = 0; i < students.size(); i++) {
                System.out.println("| " + (i + 1) + " " + students.get(i).getRegistrationSummary());
            }
            System.out.println("| 0 Cancel");
            String text = readLine("Select option: ");
            try {
                int n = Integer.parseInt(text);
                if (n == 0) {
                    return null;
                }
                if (n >= 1 && n <= students.size()) {
                    return students.get(n - 1);
                }
                System.out.println("! Please choose a number from the list.");
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a number.");
            }
        }
    }


    private static void paymentHistory() throws PrerequisiteMissingException {
        Student s = chooseStudent();
        if (s == null) {
            return;
        }
        System.out.println("========== PAYMENT HISTORY: " + s.getDisplayName() + " ==========");
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        for (FeePayment p : payments) {
            if (p.getStudent() == s) {
                System.out.println("   " + p.getPaymentDate() + " | " + p.getAmount()
                        + " | " + p.getPaymentMethod() + " | receipt " + p.getReceiptNumber());
                total = total.add(p.getAmount());
                count++;
            }
        }
        if (count == 0) {
            System.out.println("   No payment recorded for this student.");
        } else {
            System.out.println("   TOTAL PAID (" + count + " payments): " + total);
        }
    }


    private static void showAvailableBooks() {
        System.out.println("========== AVAILABLE BOOKS ==========");
        int count = 0;
        for (LibraryBook b : books) {
            if (b.getAvailableCopies() > 0) {
                System.out.println("   " + b.getRegistrationSummary());
                count++;
            }
        }
        if (count == 0) {
            System.out.println("   No book is available right now.");
        }
    }


    private static void demonstratePolymorphism() {
        System.out.println("========== POLYMORPHISM DEMO ==========");

        // Part 1: one List<Person> holding Students, Teachers and Administrators
        List<Person> people = new ArrayList<>();
        people.addAll(students);
        people.addAll(teachers);
        people.addAll(administrators);

        System.out.println("--- PART 1: List<Person> (superclass polymorphism) ---");
        if (people.isEmpty()) {
            System.out.println("   Register a student, teacher or administrator first.");
        } else {
            for (Person p : people) {
                // The same method call gives a different answer for each real type
                System.out.println("   " + p.getRoleDescription() + ": " + p.getDisplayName()
                        + " | active = " + p.isActive());
                if (p instanceof AcademicMember) {
                    System.out.println("      academic category: "
                            + ((AcademicMember) p).getAcademicCategory());
                }
            }
            System.out.println("   printDetails() called on the first person (overridden method):");
            people.get(0).printDetails();
        }

        List<Identifiable> everything = allRecords();
        System.out.println("--- PART 2: List<Identifiable> (interface polymorphism) ---");
        if (everything.isEmpty()) {
            System.out.println("   No records yet.");
        } else {
            for (Identifiable item : everything) {
                System.out.println("   " + item.getId() + " | " + item.getDisplayName()
                        + " | active = " + item.isActive());
            }
        }


        System.out.println("--- PART 3: Explanations ---");
        System.out.println("   * Person and AcademicMember are abstract. They have unfinished methods");
        System.out.println("     (getRoleDescription / getAcademicCategory), so Java refuses new Person(...).");
        System.out.println("   * Constructor chaining: new Student(...) calls super(...) in AcademicMember,");
        System.out.println("     which calls super(...) in Person.");
        System.out.println("   * Overriding: validate() and printDetails() are rewritten in each subclass,");
        System.out.println("     and they call super.validate() / super.printDetails() first.");
    }
}