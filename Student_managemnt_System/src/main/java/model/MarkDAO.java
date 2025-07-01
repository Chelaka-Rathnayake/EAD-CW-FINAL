/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class MarkDAO {
    public boolean addMark(Mark mark) {
        String sql = "INSERT INTO marks (student_id, student_name, subject1, subject2, subject3, subject4, subject5) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mark.getStudentId());
            ps.setString(2, mark.getStudentName());
            ps.setInt(3, mark.getSubject1());
            ps.setInt(4, mark.getSubject2());
            ps.setInt(5, mark.getSubject3());
            ps.setInt(6, mark.getSubject4());
            ps.setInt(7, mark.getSubject5());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteMarksByStudentId(String studentId) {
    String sql = "DELETE FROM marks WHERE student_id = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, studentId);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    public boolean updateMark(Mark mark) {
    String sql = "UPDATE marks SET student_name=?, subject1=?, subject2=?, subject3=?, subject4=?, subject5=? WHERE student_id=?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, mark.getStudentName());
        ps.setInt(2, mark.getSubject1());
        ps.setInt(3, mark.getSubject2());
        ps.setInt(4, mark.getSubject3());
        ps.setInt(5, mark.getSubject4());
        ps.setInt(6, mark.getSubject5());
        ps.setString(7, mark.getStudentId());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
public DefaultTableModel getAllMarksTableModel() {
    DefaultTableModel model = new DefaultTableModel();
    try (Connection conn = DatabaseManager.getConnection();
     PreparedStatement pst = conn.prepareStatement("SELECT * FROM marks");
     ResultSet rs = pst.executeQuery()) {

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

} catch (SQLException e) {
    e.printStackTrace();
}

return model;

}

public DefaultTableModel getMarksByStudentId(String studentId) {
    DefaultTableModel model = new DefaultTableModel();
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM marks WHERE student_id = ?")) {

        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();

        for (int i = 1; i <= colCount; i++) {
            model.addColumn(meta.getColumnName(i));
        }

        while (rs.next()) {
            Object[] row = new Object[colCount];
            for (int i = 1; i <= colCount; i++) {
                row[i-1] = rs.getObject(i);
            }
            model.addRow(row);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return model;
}

  public Mark getMarkByStudentId(String studentId) {
    String sql = "SELECT * FROM marks WHERE student_id = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Mark(
                rs.getString("student_id"),
                rs.getString("student_name"),
                rs.getInt("subject1"),
                rs.getInt("subject2"),
                rs.getInt("subject3"),
                rs.getInt("subject4"),
                rs.getInt("subject5")
            );
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}
public List<Mark> getAllMarksByCoursePrefix(String coursePrefix) {
    List<Mark> marksList = new ArrayList<>();
    String sql = "SELECT * FROM marks WHERE student_id LIKE ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, coursePrefix.toUpperCase() + "-%");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Mark mark = new Mark(
                rs.getString("student_id"),
                rs.getString("student_name"),
                rs.getInt("subject1"),
                rs.getInt("subject2"),
                rs.getInt("subject3"),
                rs.getInt("subject4"),
                rs.getInt("subject5")
            );
            marksList.add(mark);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return marksList;
}
  
}
