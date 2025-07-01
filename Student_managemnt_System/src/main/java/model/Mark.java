/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author User
 */
public class Mark {
    private int id;
    private String studentId;
    private String studentName;
    private int subject1, subject2, subject3, subject4, subject5;

    
    public Mark(String studentId, String studentName, int s1, int s2, int s3, int s4, int s5) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.subject1 = s1;
        this.subject2 = s2;
        this.subject3 = s3;
        this.subject4 = s4;
        this.subject5 = s5;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getSubject1() {
        return subject1;
    }

    public void setSubject1(int subject1) {
        this.subject1 = subject1;
    }

    public int getSubject2() {
        return subject2;
    }

    public void setSubject2(int subject2) {
        this.subject2 = subject2;
    }

    public int getSubject3() {
        return subject3;
    }

    public void setSubject3(int subject3) {
        this.subject3 = subject3;
    }

    public int getSubject4() {
        return subject4;
    }

    public void setSubject4(int subject4) {
        this.subject4 = subject4;
    }

    public int getSubject5() {
        return subject5;
    }

    public void setSubject5(int subject5) {
        this.subject5 = subject5;
    }
    public double getGPA() {
    int total = subject1 + subject2 + subject3 + subject4 + subject5;
    double avg = total / 5.0;
    return Math.round((avg / 25.0) * 100.0) / 100.0;  
}
    public Integer[] getSubjectArray() {
    return new Integer[] { subject1, subject2, subject3, subject4, subject5 };
}
}
