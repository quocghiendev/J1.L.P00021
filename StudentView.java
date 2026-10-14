package view;

import java.util.List;

import model.Student;
import util.Validation;

public class StudentView {

    private static final int MIN_STUDENTS_PER_INPUT = 10;

    private static final int MAX_STUDENTS_PER_INPUT = 100;

    /**
     * Mẫu định dạng một dòng của bảng sinh viên.
     */
    private static final String STUDENT_FORMAT = "%-25s %-10s %-10s";

    private static final String RECORD_FORMAT = "%-5s %-10s %-25s %-10s %-10s";

    /**
     * Đường kẻ ngăn cách tiêu đề và nội dung bảng.
     */
    private static final String SEPARATOR
            = "------------------------------------------------";

    private final Validation validation;

    public StudentView() {
        validation = new Validation();
    }

    public void showMenu() {
        showMessage("\n========== WELCOME TO STUDENT MANAGEMENT ==========");
        showMessage("1. Create");
        showMessage("2. Find and Sort");
        showMessage("3. Update/Delete");
        showMessage("4. Report");
        showMessage("5. Exit");
        showMessage("===================================================");
    }

    public int inputChoice(int min, int max) {
        showMessage("Please choose from " + min + " To " + max);
        return validation.inputInt(min, max);
    }

    public String inputStudentId() {
        showMessage("Enter ID:");
        return validation.inputString();
    }

    public Student inputStudentDetails(String id) {
        showMessage("Enter student name:");
        String name = validation.inputString();

        showMessage("Enter semester (" + Student.MIN_SEMESTER + "-"
                + Student.MAX_SEMESTER + "):");
        int semester = validation.inputInt(Student.MIN_SEMESTER,
                Student.MAX_SEMESTER);

        String course = inputCourse();

        return new Student(id, name, semester, course);
    }

    private String inputCourse() {
        showMessage("Choose course:");
        for (int i = 0; i < Student.COURSES.length; i++) {
            showMessage((i + 1) + ". " + Student.COURSES[i]);
        }

        int choice = validation.inputInt(1, Student.COURSES.length);
        return Student.COURSES[choice - 1];
    }

    public String inputSearchName() {
        showMessage("Enter student name or a part of name:");
        return validation.inputString();
    }

    public boolean inputContinue() {
        showMessage("Do you want to continue (Y/N)?");
        return validation.inputOneOf('Y', 'N') == 'Y';
    }

    public char inputUpdateOrDelete() {
        showMessage("Do you want to update (U) or delete (D) student?");
        return validation.inputOneOf('U', 'D');
    }

    public int inputRecordNumber(int max) {
        showMessage("Choose a record number (1-" + max + "):");
        return validation.inputInt(1, max);
    }

    public void displayStudents(List<Student> students) {
        showMessage(String.format(STUDENT_FORMAT,
                "Student name", "Semester", "Course"));
        showMessage(SEPARATOR);

        for (Student student : students) {
            showMessage(String.format(STUDENT_FORMAT,
                    student.getName(),
                    student.getSemester(),
                    student.getCourseName()));
        }
    }

    public void displayRecords(List<Student> students) {
        showMessage(String.format(RECORD_FORMAT,
                "No", "ID", "Student name", "Semester", "Course"));
        showMessage(SEPARATOR);

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);

            showMessage(String.format(RECORD_FORMAT,
                    (i + 1),
                    student.getId(),
                    student.getName(),
                    student.getSemester(),
                    student.getCourseName()));
        }
    }

    public void displayReportRow(String name, String course, int total) {
        showMessage(name + " | " + course + " | " + total);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
