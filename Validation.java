/*
 * Validation.java
 *
 * Phiên bản: 1.3
 * Ngày cập nhật: 30/09/2026
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
 * Cung cấp các phương thức đọc dữ liệu có kiểm tra hợp lệ: chuỗi không rỗng, số
 * nguyên và số thực trong một khoảng cho trước.
 * <p>
 * Lớp không in lời nhắc nhập; lời nhắc do View hiển thị qua showMessage trước
 * khi gọi. Khi dữ liệu sai, lớp báo lỗi và đọc lại cho tới khi hợp lệ, vì vậy
 * nơi gọi luôn nhận được giá trị đúng.
 *
 * @version 1.3 30 Sep 2026
 * @author [Họ tên sinh viên]
 */
public class Validation {

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
     * Đọc một chuỗi khác rỗng. Khoảng trắng ở hai đầu được loại bỏ.
     *
     * @return chuỗi người dùng nhập, đã loại bỏ khoảng trắng hai đầu
     * @throws IllegalStateException nếu luồng nhập đã đóng
     */
    public String inputString() {
        while (true) {
            String input = readLine();

            if (!input.isEmpty() && isLettersAndSpace(input)) {
                return input;
            }
            else if (input.isEmpty()) {
                System.out.println("Input not empty!");
                System.out.println("Re-input: ");
                continue;
            }
            System.out.println("Must be String, Please! re-input:");
        }
    }

    /**
     * Kiểm tra chuỗi chỉ gồm chữ cái và dấu cách.
     *
     * @param text chuỗi cần kiểm tra
     * @return true nếu mọi ký tự là chữ cái hoặc dấu cách
     */
    private boolean isLettersAndSpace(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (!Character.isLetter(c) && (c != ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Đọc một mã sinh viên gồm 2 chữ cái rồi 6 chữ số (ví dụ HE205169). Sai
     * định dạng thì báo lỗi và nhập lại.
     *
     * @return mã hợp lệ, đã loại bỏ khoảng trắng hai đầu
     * @throws IllegalStateException nếu luồng nhập đã đóng
     */
    public String inputStudentCode() {
        while (true) {

            String input = readLine();

            if ((input.length() == 8)
                    && isAllLetters(input, 0, 2)
                    && isAllDigits(input, 2, 8)) {
                return input;
            } else if (input.isEmpty()) {
                System.out.println("Input not empty!");
                System.out.println("Re-input: ");
                continue;
            }
            System.out.println("Not format for ID!");
            System.out.println("Re-input: ");
        }
    }

    /**
     * Kiểm tra các ký tự từ vị trí from đến to - 1 đều là chữ cái a-z, A-Z.
     *
     * @param text chuỗi cần kiểm tra
     * @param from vị trí bắt đầu (tính cả)
     * @param to vị trí kết thúc (không tính)
     * @return true nếu mọi ký tự trong đoạn là chữ cái
     */
    private boolean isAllLetters(String text, int from, int to) {
        for (int i = from; i < to; i++) {
            char c = text.charAt(i);

            if (!(((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Kiểm tra các ký tự từ vị trí from đến to - 1 đều là chữ số 0-9.
     *
     * @param text chuỗi cần kiểm tra
     * @param from vị trí bắt đầu (tính cả)
     * @param to vị trí kết thúc (không tính)
     * @return true nếu mọi ký tự trong đoạn là chữ số
     */
    private boolean isAllDigits(String text, int from, int to) {
        for (int i = from; i < to; i++) {
            char c = text.charAt(i);

            if ((c < '0') || (c > '9')) {
                return false;
            }
        }
        return true;
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

    public char inputOneOf(char a, char b) {
        while (true) {
            String input = readLine();
            if (input.length() == 1
                    && (input.charAt(0) == b || input.charAt(0) == a)) {
                return input.charAt(0);
            }

            System.out.println("please input only " + a + " or " + b);
        }
    }
}
