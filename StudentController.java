/*
 * StudentController.java
 *
 * Phiên bản: 2.2
 * Ngày cập nhật: 16/10/2026
 * Tác giả: [Họ tên sinh viên] - [Mã sinh viên]
 *
 * Copyright (c) 2026 [Họ tên sinh viên]. All rights reserved.
 *
 * Mô tả: Thành phần Controller của chương trình Quản lý sinh viên
 * (J1.L.P0021). Lớp điều phối luồng chương trình và xử lý nghiệp vụ trên
 * danh sách sinh viên.
 */
package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.Student;
import view.StudentView;


/**
 * Điều phối giữa View và Model của chương trình Quản lý sinh viên.
 * Đây là thành phần Controller trong kiến trúc MVC: nhận lựa chọn từ
 * View, thực hiện nghiệp vụ (thêm, tìm kiếm, sắp xếp, cập nhật, xóa,
 * thống kê) trên danh sách Student, rồi yêu cầu View hiển thị kết quả.
 * <p>
 * Một mã sinh viên có thể xuất hiện ở nhiều bản ghi (mỗi bản ghi là một
 * khóa học sinh viên đã đăng ký). Các bản ghi cùng mã luôn cùng tên: khi
 * thêm, tên phải khớp với bản ghi cũ; khi cập nhật, tên mới được đổi cho
 * mọi bản ghi cùng mã.
 *
 * @version 2.2 16 Oct 2026
 * @author [Họ tên sinh viên]
 */
public class StudentController {

    /**
     * Ngưỡng số sinh viên: khi danh sách có nhiều hơn ngưỡng này, chương
     * trình mới hỏi người dùng có tiếp tục nhập hay không.
     */
    private static final int MIN_STUDENTS = 10;

    /** Danh sách sinh viên đang được quản lý (lưu trong bộ nhớ). */
    private final List<Student> students;

    /** View dùng để nhập và xuất dữ liệu. */
    private final StudentView view;

    /**
     * Tạo Controller với danh sách rỗng và View mặc định.
     */
    public StudentController() {
        students = new ArrayList<>();
        view = new StudentView();
    }

    /**
     * Chạy chương trình cho tới khi người dùng chọn thoát hoặc luồng nhập
     * bị đóng.
     */
    public void run() {
        try {
            runMenu();
        } catch (IllegalStateException e) {

            /*
             * Luồng nhập đã đóng (Ctrl+D / Ctrl+Z hoặc file input hết dữ
             * liệu): không thể đọc tiếp nên thông báo và kết thúc.
             */
            view.showMessage("\n" + e.getMessage()
                    + " Program terminated.");
        }
    }

    /**
     * Lặp hiển thị menu, đọc lựa chọn và gọi chức năng tương ứng cho tới
     * khi người dùng chọn thoát.
     */
    private void runMenu() {
        while (true) {
            view.showMenu();

            /*
             * Menu có 5 chức năng đánh số từ 1 đến 5; View đọc và kiểm tra
             * để lựa chọn luôn nằm trong khoảng này.
             */
            int choice = view.inputChoice(1, 5);

            switch (choice) {
            case 1:
                createStudents();
                break;
            case 2:
                findAndSort();
                break;
            case 3:
                updateOrDelete();
                break;
            case 4:
                report();
                break;
            case 5:
                view.showMessage("Program terminated.");
                return;
            default:

                /* Không xảy ra vì View đã kiểm tra lựa chọn hợp lệ. */
                view.showMessage("Invalid choice.");
                break;
            }
        }
    }

    /**
     * Thêm sinh viên vào danh sách.
     * Bản ghi sai tên so với mã đã có, hoặc trùng cả mã, học kỳ và khóa
     * học, bị từ chối và người dùng phải nhập lại.
     * Khi danh sách có nhiều hơn MIN_STUDENTS sinh viên, sau mỗi lần thêm
     * chương trình hỏi có tiếp tục hay không: Y để nhập tiếp, N để quay
     * về menu chính.
     */
    private void createStudents() {
        while (true) {
            view.showMessage("\nStudent " + (students.size() + 1));

            String id = view.inputStudentId();
            Student student = view.inputStudentDetails(id);
            String error = checkConflict(student); // check các trường hợp tại đây 

            if (error != null) { // nếu có th lỗi 

                /* Bản ghi không hợp lệ: không thêm, yêu cầu nhập lại. */
                view.showMessage(error + " Try again.");
                continue;
            }

            students.add(student);
            view.showMessage("Student added successfully.");

            if ((students.size() > MIN_STUDENTS) // cho đến khi lớn hơn 10 và người dùng chọn N
                    && !view.inputContinue()) {
                return;
            }
        }
    }

    /**
     * Kiểm tra bản ghi mới có mâu thuẫn với các bản ghi đã có hay không.
     * <p>
     * Hai quy tắc, so sánh không phân biệt chữ hoa, chữ thường:
     * <ul>
     * <li>Cùng mã thì phải cùng tên (một mã là một sinh viên).</li>
     * <li>Không có hai bản ghi trùng cả mã, học kỳ và khóa học.</li>
     * </ul>
     *
     * @param candidate bản ghi cần kiểm tra
     * @return thông báo lỗi nếu có mâu thuẫn, ngược lại null
     */
    private String checkConflict(Student candidate) {
        List<Student> sameId = findById(candidate.getId());

        /* Các bản ghi cùng mã luôn cùng tên nên chỉ cần xét bản ghi đầu. */
        if (!sameId.isEmpty() &&
                !sameId.get(0).getName().equalsIgnoreCase(candidate.getName())) {
            return "ID " + candidate.getId() + " already belongs to "
                    + sameId.get(0).getName() + ".";
        }
        if (isDuplicate(candidate.getId(), candidate.getSemester(),
                candidate.getCourseName(), null)) {
            return "This record already exists.";
        }
        return null;
    }

    /**
     * Kiểm tra đã có bản ghi nào trùng mã, học kỳ và khóa học hay chưa.
     * Không cần so sánh tên vì các bản ghi cùng mã luôn cùng tên.
     * So sánh mã và khóa học không phân biệt chữ hoa, chữ thường.
     *
     * @param id       mã sinh viên
     * @param semester học kỳ
     * @param course   tên khóa học
     * @param ignored  bản ghi bỏ qua khi so sánh (bản ghi đang được cập
     *                 nhật), hoặc null nếu không có
     * @return true nếu đã có bản ghi trùng, ngược lại false
     */
    private boolean isDuplicate(String id, String semester, String course,
            Student ignored) {
        for (Student student : students) {
            if ((student != ignored)
                    && student.getId().equalsIgnoreCase(id)
                    && (student.getSemester().equalsIgnoreCase(semester) )
                    && student.getCourseName().equalsIgnoreCase(course)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tìm sinh viên có tên chứa từ khóa (không phân biệt hoa thường),
     * sắp xếp kết quả theo tên rồi hiển thị.
     * Chỉ danh sách kết quả được sắp xếp, danh sách gốc giữ nguyên.
     */
    private void findAndSort() {
        String keyword = view.inputSearchName().toLowerCase();
        List<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (student.getName().toLowerCase().contains(keyword)) {
                result.add(student);
            }
        }

        if (result.isEmpty()) {
            view.showMessage("No student found.");
            return;
        }

        sortByName(result);
        view.showMessage("\nSearch result:");
        view.displayStudents(result);
    }

    /**
     * Sắp xếp danh sách theo tên sinh viên tăng dần, không phân biệt
     * chữ hoa, chữ thường.
     *
     * @param list danh sách cần sắp xếp (được sắp xếp tại chỗ)
     */
    private void sortByName(List<Student> list) {
        Collections.sort(list, new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return a.getName().compareToIgnoreCase(b.getName());
            }
        });
    }

    /**
     * Tìm sinh viên theo mã, rồi cập nhật hoặc xóa theo lựa chọn của
     * người dùng.
     * Nếu mã có nhiều bản ghi (nhiều khóa học), người dùng chọn một bản
     * ghi trong danh sách.
     */
    private void updateOrDelete() {
        String id = view.inputStudentId();
        List<Student> matches = findById(id);

        if (matches.isEmpty()) {
            view.showMessage("No student found with ID " + id + ".");
            return;
        }

        Student target = selectRecord(matches);
        char action = view.inputUpdateOrDelete();

        if (action == 'U') {
            updateStudent(target);
        } else {
            deleteStudent(target);
        }
    }

    /**
     * Tìm tất cả bản ghi có mã trùng với mã cho trước (không phân biệt
     * hoa thường).
     *
     * @param id mã sinh viên cần tìm
     * @return danh sách bản ghi tìm thấy, rỗng nếu không có
     */
    private List<Student> findById(String id) {
        List<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                result.add(student);
            }
        }
        return result;
    }

    /**
     * Chọn một bản ghi từ danh sách khớp mã.
     * Chỉ có một bản ghi thì chọn luôn, nhiều bản ghi thì hiển thị để
     * người dùng chọn theo số thứ tự.
     *
     * @param matches danh sách bản ghi khớp mã, không rỗng
     * @return bản ghi được chọn
     */
    private Student selectRecord(List<Student> matches) {
        if (matches.size() == 1) {
            return matches.get(0);
        }

        view.showMessage("\nThis ID has " + matches.size() + " records:");
        view.displayRecords(matches);

        int number = view.inputRecordNumber(matches.size());
        return matches.get(number - 1);
    }

    /**
     * Cập nhật bản ghi: nhập lại họ tên, học kỳ, khóa học (mã giữ
     * nguyên). Nếu học kỳ và khóa học mới trùng một bản ghi khác của cùng
     * mã thì việc cập nhật bị hủy và bản ghi giữ nguyên.
     * Họ tên mới được áp dụng cho mọi bản ghi cùng mã.
     *
     * @param target bản ghi cần cập nhật
     */
    private void updateStudent(Student target) {
        Student updated = view.inputStudentDetails(target.getId());

        if (isDuplicate(target.getId(), updated.getSemester(),
                updated.getCourseName(), target)) {
            view.showMessage("This record already exists. Update cancelled.");
            return;
        }

        /* Đổi tên cho mọi bản ghi cùng mã để các bản ghi luôn khớp tên. */
        for (Student student : findById(target.getId())) {
            student.setName(updated.getName());
        }
        target.setSemester(updated.getSemester());
        target.setCourseName(updated.getCourseName());
        view.showMessage("Student updated successfully.");
    }

    /**
     * Xóa bản ghi khỏi danh sách.
     *
     * @param target bản ghi cần xóa
     */
    private void deleteStudent(Student target) {
        students.remove(target);
        view.showMessage("Student deleted successfully.");
    }

    /**
     * Thống kê và hiển thị số lần mỗi sinh viên học từng khóa học, theo
     * dạng: tên | khóa học | tổng số. Mỗi cặp (tên, khóa học) chỉ xuất
     * hiện một dòng, theo thứ tự xuất hiện đầu tiên trong danh sách.
     */
    private void report() {
        if (students.isEmpty()) {
            view.showMessage("Student list is empty.");
            return;
        }

        view.showMessage("\nReport:");
        for (int i = 0; i < students.size(); i++) {
            Student current = students.get(i);

            if (isReported(current, i)) {
                continue;
            }

            int total = 0;
            for (Student student : students) {
                if (isSameNameAndCourse(student, current)) {
                    total++;
                }
            }
            view.displayReportRow(current.getName(), current.getCourseName(),
                    total);
        }
    }

    /**
     * Kiểm tra cặp (tên, khóa học) của một bản ghi đã được báo cáo ở
     * dòng trước hay chưa, bằng cách tìm trong các bản ghi đứng trước nó.
     *
     * @param current bản ghi cần kiểm tra
     * @param index   vị trí của bản ghi trong danh sách
     * @return true nếu một bản ghi đứng trước đã có cùng tên và khóa học
     */
    private boolean isReported(Student current, int index) {
        for (int i = 0; i < index; i++) {
            if (isSameNameAndCourse(students.get(i), current)) {
                return true;
            }
        }
        return false;
    }

    /**
     * So sánh hai bản ghi có cùng tên và cùng khóa học hay không (không
     * phân biệt chữ hoa, chữ thường).
     *
     * @param a bản ghi thứ nhất
     * @param b bản ghi thứ hai
     * @return true nếu cùng tên và cùng khóa học
     */
    private boolean isSameNameAndCourse(Student a, Student b) {
        return a.getName().equalsIgnoreCase(b.getName())
                && a.getCourseName().equalsIgnoreCase(b.getCourseName());
    }
}