/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author User
 */
import java.sql.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
public class StudentDAO {
        public void addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO students (id, name, phone, email, dob, address, course) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getDob());
            ps.setString(6, s.getAddress());
            ps.setString(7, s.getCourse());
            ps.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("dob"),
                    rs.getString("address"),
                    rs.getString("course")
                );
                list.add(s);
            }
        }
        return list;
    }

    public void updateStudent(Student s) throws SQLException {
        String sql = "UPDATE students SET name=?, phone=?, email=?, dob=?, address=?, course=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getDob());
            ps.setString(5, s.getAddress());
            ps.setString(6, s.getCourse());
            ps.setString(7, s.getId());
            ps.executeUpdate();
        }
    }

    public boolean deleteStudent(String id) {
    String sql = "DELETE FROM students WHERE id = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, id);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    public DefaultTableModel getStudentTableModel(String studentId) {
    DefaultTableModel model = new DefaultTableModel();

    String sql;
    boolean filter = (studentId != null && !studentId.equals("__"));

    if (filter) {
        sql = "SELECT * FROM students WHERE id = ?";
    } else {
        sql = "SELECT * FROM students";
    }

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        if (filter) {
            pst.setString(1, studentId);
        }

        try (ResultSet rs = pst.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(meta.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return model;
}

   public String generateStudentId(String courseName) {
    String prefix = getCoursePrefix(courseName);
    int count = getStudentCountForCourse(courseName) + 1;
    return prefix + "-" + String.format("%03d", count);
}

    private String getCoursePrefix(String courseName) {
        switch (courseName.toLowerCase()) {
            case "software engineering": return "SE";
            case "computer science": return "CS";
            case "business management": return "BM";
            case "agri": return "AG";
            default: return "GEN";
        }
    }

    public int getStudentCountForCourse(String courseName) {
        String sql = "SELECT COUNT(*) FROM students WHERE course = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

        public String getStudentNameById(String studentId) {
    String sql = "SELECT name FROM students WHERE id = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("name");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "";
}
        public List<String> getAllStudentIds() {
    List<String> studentIds = new ArrayList<>();
    String sql = "SELECT id FROM students";

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            studentIds.add(rs.getString("id"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return studentIds;
}
    public String getStudentCourseById(String studentId) {
    String sql = "SELECT course FROM students WHERE id = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("course");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "";
}


}




