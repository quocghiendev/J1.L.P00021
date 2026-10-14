package model;
public class Student {
    public static final int MIN_SEMESTER = 1;
    public static final int MAX_SEMESTER = 9;
    public static final String[] COURSES = {"Java", ".Net", "C/C++"};
    
    private String id;
    private String name;
    private int semester;
    private String courseName;

    public Student() {
    }

    public Student(String id, String name, int semester, String courseName) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.courseName = courseName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSemester() {
        return semester;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name=" + name + ", semester=" + semester + ", courseName=" + courseName + '}';
    }
    
    
}
