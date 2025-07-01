/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.Stdmanagement;
import javax.swing.table.DefaultTableModel;
import model.Mark;
import model.MarkDAO;
import model.StudentDAO;

/**
 *
 * @author User
 */
public class MarkController {
     private MarkDAO dao;
    private Stdmanagement view;

    
    
    public boolean saveMark(Mark mark) {
        
        MarkDAO dao = new MarkDAO();
        return dao.addMark(mark);
    }

   public boolean deleteMark(String studentId) {
    MarkDAO dao = new MarkDAO();
    return dao.deleteMarksByStudentId(studentId);
   

}
   public boolean updateMark(Mark mark) {
    MarkDAO dao = new MarkDAO();
    return dao.updateMark(mark);
}

}



