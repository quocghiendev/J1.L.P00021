/*
 * Validation.java
 *
 * Phiên bản: 1.4
 * Ngày cập nhật: 28/10/2026
 * Tác giả: [Họ tên sinh viên] - [Mã sinh viên]
 *
 * Copyright (c) 2026 [Họ tên sinh viên]. All rights reserved.
 *
 * Mô tả: Lớp tiện ích gom các thao tác đọc và kiểm tra dữ liệu nhập
 * từ bàn phím, dùng chung cho tầng View.
 */
package util;

import java.util.Scanner;

/**
 * Cung cấp các phương thức đọc dữ liệu có kiểm tra hợp lệ: họ tên, mã sinh
 * viên (dùng biểu thức chính quy), số nguyên trong một khoảng cho trước và
 * lựa chọn một trong hai ký tự.
 * <p>
 * Lớp không in lời nhắc nhập; lời nhắc do View hiển thị qua showMessage trước
 * khi gọi. Khi dữ liệu sai, lớp báo lỗi và đọc lại cho tới khi hợp lệ, vì vậy
 * nơi gọi luôn nhận được giá trị đúng.
 *
 * @version 1.4 28 Oct 2026
 * @author [Họ tên sinh viên]
 */
public class Validation {

    /**
     * Mẫu mã sinh viên: tiền tố HE, HS hoặc HA, theo sau là đúng 6 chữ số
     * (ví dụ HE205169). Tổng cộng 8 ký tự, phân biệt chữ hoa, chữ thường.
     */
    public static final String STUDENT_CODE_PATTERN = "^(?i:HE|HS|HA)\\d{6}$";

    /**
     * Mẫu họ tên: chỉ gồm chữ cái (kể cả tiếng Việt có dấu) và dấu cách,
     * không chứa chữ số hay ký tự đặc biệt. \p{L} khớp mọi chữ cái Unicode,
     * \s khớp khoảng trắng.
     */
    public static final String NAME_PATTERN = "^[\\p{L}\\s]+$";

    public static final String SEMESTER_PATTERN = "^(?i:SP|SU|FA)\\d{2}$";
    /*
     * Chỉ dùng một Scanner cho toàn bộ chương trình. Tạo nhiều Scanner
     * cùng đọc System.in có thể làm mất dữ liệu đệm giữa các lần nhập.
     */
    /**
     * Bộ đọc dữ liệu từ bàn phím.
     */
    private final Scanner sc;

    /**
     * Tạo đối tượng Validation đọc dữ liệu từ bàn phím.
     */
    public Validation() {
        sc = new Scanner(System.in);
    }

    /**
     * Đọc một dòng từ bàn phím và loại bỏ khoảng trắng ở hai đầu. Mọi phương
     * thức nhập đều đọc qua đây, nên việc kiểm tra luồng nhập đã đóng chỉ cần
     * viết một lần.
     *
     * @return dòng vừa đọc, đã loại bỏ khoảng trắng hai đầu
     * @throws IllegalStateException nếu luồng nhập đã đóng (người dùng bấm
     * Ctrl+D / Ctrl+Z hoặc file input hết dữ liệu)
     */
    private String readLine() {

        /*
         * Nếu không kiểm tra, sc.nextLine() sẽ ném NoSuchElementException
         * khi hết dữ liệu và chương trình dừng với stack trace. Ở đây ném
         * IllegalStateException kèm thông báo rõ ràng để Controller bắt
         * và kết thúc chương trình gọn gàng.
         */
        if (!sc.hasNextLine()) {
            throw new IllegalStateException("Input stream is closed.");
        }
        return sc.nextLine().trim();
    }

    /**
     * Đọc một họ tên hợp lệ: không rỗng, chỉ gồm chữ cái và dấu cách.Khoảng trắng ở hai đầu được loại bỏ.
     *
     * @param regex
     * @param massage
     * @param error
     * @return chuỗi người dùng nhập, đã loại bỏ khoảng trắng hai đầu
     * @throws IllegalStateException nếu luồng nhập đã đóng
     */
    public String inputString(String regex, String massage, String error) {
        while (true) {
            System.out.println(massage);
            String input = readLine();

            if (input.matches(regex)) {
                return input;
            }
            if (input.isEmpty()) {
                System.out.println("Input not empty!");
                System.out.println("Re-input: ");
            } else {
                System.out.println(error);
            }
            System.out.println("Re-input: ");
        }
    }
    

    /**
     * Đọc một số nguyên nằm trong đoạn [min, max].
     *
     * @param min giá trị nhỏ nhất được chấp nhận
     * @param max giá trị lớn nhất được chấp nhận
     * @return số nguyên hợp lệ người dùng nhập
     * @throws IllegalStateException nếu luồng nhập đã đóng
     */
    public int inputInt(int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(readLine());

                if ((value >= min) && (value <= max)) {
                    return value;
                }
                System.out.println("Value must be from " + min + " to "
                        + max + ". Please re-enter:");
            } catch (NumberFormatException e) {

                /* Chuỗi nhập không phải số nguyên: báo lỗi và đọc lại. */
                System.out.println("Please enter an integer:");
            }
        }
    }
}