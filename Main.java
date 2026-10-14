/*
 * Main.java
 *
 * Phiên bản: 1.1
 * Ngày cập nhật: 30/09/2026
 * Tác giả: [Họ tên sinh viên] - [Mã sinh viên]
 *
 * Copyright (c) 2026 [Họ tên sinh viên]. All rights reserved.
 *
 * Mô tả: Điểm khởi chạy của chương trình Quản lý sinh viên theo kiến
 * trúc MVC.
 */
package main;

import controller.StudentController;


/**
 * Lớp khởi động chương trình Quản lý sinh viên.
 * Lớp chỉ tạo Controller và giao quyền điều khiển cho nó; không chứa
 * nghiệp vụ hay thao tác nhập/xuất.
 *
 * @version 1.1 30 Sep 2026
 * @author [Họ tên sinh viên]
 */
public class Main {

    /**
     * Hàm main: tạo Controller và chạy chương trình.
     *
     * @param args tham số dòng lệnh (không sử dụng)
     */
    public static void main(String[] args) {
        StudentController controller = new StudentController();

        controller.run();
    }
}
