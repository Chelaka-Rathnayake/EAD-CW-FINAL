/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import model.*;
import View.Stdmanagement;
import java.awt.event.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author User
 */
public class StudentController {
    
        private StudentDAO dao;
    private Stdmanagement view;

    public StudentController(StudentDAO dao, Stdmanagement view) {
        this.dao = dao;
        this.view = view;

        view.btnadd.addActionListener(e -> addStudent());
        view.btnview.addActionListener(e -> loadStudents());
        view.btnremove.addActionListener(e -> deleteStudent());
        view.btnupdate.addActionListener(e -> updateStudent());
   

        System.out.println(" Controller is now active");
        
    }
    
    private void addStudent() {
        try {
            Student s = view.getStudentFromForm();
            if (s.getId().isBlank()) {
            String newId = dao.generateStudentId(s.getCourse());
            s.setId(newId);
}

            dao.addStudent(s);
            view.showMessage("Student added with ID: " + s.getId());
            view.clearForm();
            view.refreshStudentIdCombo();
        } catch (Exception ex) {
            view.showError("Error adding student: " + ex.getMessage());
        }
    }

   private void loadStudents() {
    try {
        DefaultTableModel model = dao.getStudentTableModel("__");
        System.out.println("→ Model loaded. Row count: " + model.getRowCount());
        view.setTableModel(model);
    } catch (Exception ex) {
        System.out.println(" Error in loadStudents(): " + ex.getMessage());
        view.showError("Error loading students: " + ex.getMessage());
    }
}

    private void updateStudent() {
    try {
        Student s = view.getStudentFromForm();
        if (s.getId() == null || s.getId().isBlank()) {
            view.showError("Please enter the student ID to update.");
            return;
        }
        dao.updateStudent(s);
        view.showMessage("Student updated successfully.");
        view.clearForm();
        
    } catch (Exception ex) {
        view.showError("Update failed: " + ex.getMessage());
    }
}

private void deleteStudent() {
    try {
        String id = view.getStudentFromForm().getId();
        if (id == null || id.isBlank()) {
            view.showError("Please enter the student ID to delete.");
            return;
        }

        
boolean marksDeleted = new MarkController().deleteMark(id);
boolean studentDeleted = dao.deleteStudent(id);


if (studentDeleted) {
    view.showMessage("Student deleted successfully. Marks deleted if existed.");
    view.clearForm();
} else {
    view.showError("Failed to delete student.");
}


    } catch (Exception ex) {
        view.showError("Delete failed: " + ex.getMessage());
    }
}

    
    
}
