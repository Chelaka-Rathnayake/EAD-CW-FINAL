/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

/**
 *
 * @author User
 */
import Controller.MarkController;
import Controller.PerformanceController;
import Controller.StudentController;
import java.awt.event.ItemEvent;
import javax.swing.JOptionPane;
import model.StudentDAO;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Mark;
import model.MarkDAO;
import model.Student;
import com.formdev.flatlaf.FlatLightLaf;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.DatabaseManager;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;



public class Stdmanagement extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Stdmanagement.class.getName());
    private String loggedInUserId;

    /**
     * Creates new form Stdmanagement
     */
    public Stdmanagement(boolean isAdmin, String userId) {
        this(); 
        
        this.loggedInUserId = userId;

        if (!isAdmin) {
           
            for (int i = jTabbedPane1.getTabCount() - 1; i >= 0; i--) {
                String title = jTabbedPane1.getTitleAt(i);
                if (!title.equalsIgnoreCase("Performance") && !title.equalsIgnoreCase("Reports")) {
                    jTabbedPane1.removeTabAt(i);
                }
            }
        }
    }
    public Stdmanagement() {
        initComponents();
        
        
       PerformanceController perfController = new PerformanceController(this, new StudentDAO(), new MarkDAO());
       


        
        cmbcourse.addItemListener(e -> {
        if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
        String selectedCourse = cmbcourse.getSelectedItem().toString();
        String generatedId = new StudentDAO().generateStudentId(selectedCourse);
        txtID.setText(generatedId);
    }


});
        cmbStudentId.addItemListener(e -> {
    if (e.getStateChange() == ItemEvent.SELECTED) {
        String selectedId = cmbStudentId.getSelectedItem().toString();
        String name = new StudentDAO().getStudentNameById(selectedId);
        txtStudentName.setText(name);
    }
});
        List<String> ids = new StudentDAO().getAllStudentIds();
        for (String id : ids) {
        cmbStudentId.addItem(id);
}
        btnAddMark.addActionListener(e -> {
    String studentId = cmbStudentId.getSelectedItem().toString();
    String studentName = txtStudentName.getText(); 

    try {
        int s1 = Integer.parseInt(txtsubject1.getText());
        int s2 = Integer.parseInt(txtsubject2.getText());
        int s3 = Integer.parseInt(txtsubject3.getText());
        int s4 = Integer.parseInt(txtsubject4.getText());
        int s5 = Integer.parseInt(txtsubject5.getText());

        Mark mark = new Mark(studentId, studentName, s1, s2, s3, s4, s5);
        boolean added = new MarkController().saveMark(mark);

        if (added) {
            JOptionPane.showMessageDialog(this, "Marks added successfully!");
            
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add marks.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter numeric values for all subjects.", "Input Error", JOptionPane.WARNING_MESSAGE);
    }
});
        

        setSize(1200, 600);
    
    btnmarkdelete.addActionListener(e -> {
    String studentId = (String) cmbStudentId.getSelectedItem();
    if (studentId == null || studentId.isBlank()) {
        JOptionPane.showMessageDialog(this, "Please select a student ID.");
        return;
    }

    boolean success = new Controller.MarkController().deleteMark(studentId);
    if (success) {
        JOptionPane.showMessageDialog(this, "Marks deleted successfully.");
        
        txtsubject1.setText("");
        txtsubject2.setText("");
        txtsubject3.setText("");
        txtsubject4.setText("");
        txtsubject5.setText("");
    } else {
        JOptionPane.showMessageDialog(this, "Failed to delete marks.", "Error", JOptionPane.ERROR_MESSAGE);
    }
});
    btnmarkupdate.addActionListener(e -> {
    String studentId = (String) cmbStudentId.getSelectedItem();
    if (studentId == null || studentId.isBlank()) {
        JOptionPane.showMessageDialog(this, "Please select a student ID.");
        return;
    }
    try {
        int s1 = Integer.parseInt(txtsubject1.getText());
        int s2 = Integer.parseInt(txtsubject2.getText());
        int s3 = Integer.parseInt(txtsubject3.getText());
        int s4 = Integer.parseInt(txtsubject4.getText());
        int s5 = Integer.parseInt(txtsubject5.getText());

        String studentName = txtStudentName.getText(); 

        model.Mark mark = new model.Mark(studentId, studentName, s1, s2, s3, s4, s5);
        boolean updated = new Controller.MarkController().updateMark(mark);

        if (updated) {
            JOptionPane.showMessageDialog(this, "Marks updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update marks.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter valid numeric marks.", "Input Error", JOptionPane.WARNING_MESSAGE);
    }
});
    btnmarkview.addActionListener(e -> {
    String studentId = (String) cmbStudentId.getSelectedItem();
    if (studentId == null || studentId.isBlank()) {
        JOptionPane.showMessageDialog(this, "Please select a student ID.");
        return;
    }

    MarkDAO dao = new MarkDAO();
    DefaultTableModel model = dao.getMarksByStudentId(studentId);
    setMarksTableModel(model);
});
    btnViewAllMarks.addActionListener(e -> {
    MarkDAO dao = new MarkDAO();
    DefaultTableModel model = dao.getAllMarksTableModel();
    setMarksTableModel(model);
});



    }
    public void setTableModel(javax.swing.table.DefaultTableModel model) {
    jTable1.setModel(model);    
}
    
    public void setMarksTableModel(DefaultTableModel model) {
    markstable.setModel(model);
}
   




    public void refreshStudentIdCombo() {
    cmbStudentId.removeAllItems();
    for (String id : new model.StudentDAO().getAllStudentIds()) {
        cmbStudentId.addItem(id);
    }
    }
    
    public Student getStudentFromForm() {
    return new Student(
        txtID.getText(),
        txtname.getText(),
        txtnumber.getText(),
        txtemail.getText(),
        txtbirth.getText(),
        txtaddress.getText(),
        cmbcourse.getSelectedItem().toString()
    );
}
    public void showMessage(String msg) {
    JOptionPane.showMessageDialog(this, msg);
}
    public void showError(String msg) {
    JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
}
    public void clearForm() {
    txtID.setText("");
    txtname.setText("");
    txtnumber.setText("");
    txtemail.setText("");
    txtbirth.setText("");
    txtaddress.setText("");
    cmbcourse.setSelectedIndex(0);
}
    public String getEnteredStudentId() {
    return txtStudentId.getText().trim();
}

public JButton getAnalyzeButton() {
    return btnAnalyze;
}

public JLabel getLabelName() {
    return lblName;
}
public JLabel getLabelCourse() {
    return lblCourse;
}
public JLabel getLabelTotal() {
    return lblTotal;
}

public JLabel getLabelAverage() {
    return lblAverage;
}

public JLabel getLabelGPA() {
    return lblGPA;
}

public JLabel getLabelGrade() {
    return lblGrade;
}

public JLabel getLabelRank() {
    return lblRank;
}

public JLabel getLabelSubjects() {
    return lblSubjects;
}

public JLabel getLabelMissing() {
    return lblMissing;
}

public JLabel getLabelFailed() {
    return lblFailed;
}

public JTextArea getAdviceArea() {
    return txtAdvice;
}

public JProgressBar getGpaProgress() {
    return gpaProgress;
}

public JPanel getChartPanel() {
    return chartPanel;
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtname = new javax.swing.JTextField();
        txtnumber = new javax.swing.JTextField();
        txtemail = new javax.swing.JTextField();
        txtbirth = new javax.swing.JTextField();
        txtaddress = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        btnadd = new javax.swing.JButton();
        btnupdate = new javax.swing.JButton();
        btnremove = new javax.swing.JButton();
        btnview = new javax.swing.JButton();
        btnclear = new javax.swing.JButton();
        cmbcourse = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnreportview = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnAddMark = new javax.swing.JButton();
        btnmarkupdate = new javax.swing.JButton();
        btnmarkdelete = new javax.swing.JButton();
        btnmarkclear = new javax.swing.JButton();
        cmbStudentId = new javax.swing.JComboBox<>();
        txtsubject1 = new javax.swing.JTextField();
        txtsubject2 = new javax.swing.JTextField();
        txtsubject3 = new javax.swing.JTextField();
        txtsubject4 = new javax.swing.JTextField();
        txtsubject5 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtStudentName = new javax.swing.JTextField();
        btnmarkview = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        markstable = new javax.swing.JTable();
        btnViewAllMarks = new javax.swing.JButton();
        viewacareport = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtStudentId = new javax.swing.JTextField();
        btnAnalyze = new javax.swing.JButton();
        gpaProgress = new javax.swing.JProgressBar();
        chartPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblCourse = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblAverage = new javax.swing.JLabel();
        lblGPA = new javax.swing.JLabel();
        lblGrade = new javax.swing.JLabel();
        lblRank = new javax.swing.JLabel();
        lblSubjects = new javax.swing.JLabel();
        lblMissing = new javax.swing.JLabel();
        lblFailed = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAdvice = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btnanalyze = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        lblRank1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 255));
        jLabel2.setText("Name");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 33, 64, 25));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 84, -1, -1));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 255));
        jLabel3.setText("Phone Number");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 97, -1, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 204, 255));
        jLabel4.setText("Email");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 145, -1, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 204, 255));
        jLabel5.setText("Date of birth");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 193, -1, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 255));
        jLabel6.setText("Address");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 241, -1, -1));

        txtname.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnameActionPerformed(evt);
            }
        });
        jPanel1.add(txtname, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 29, 180, 32));

        txtnumber.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jPanel1.add(txtnumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 90, 180, 30));

        txtemail.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jPanel1.add(txtemail, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 138, 180, 30));

        txtbirth.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jPanel1.add(txtbirth, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 186, 180, 30));

        txtaddress.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jPanel1.add(txtaddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 234, 180, 30));

        jLabel7.setForeground(new java.awt.Color(204, 204, 255));
        jLabel7.setText("Student ID");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 28, 66, 35));

        txtID.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });
        jPanel1.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(514, 18, 180, 49));

        btnadd.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnadd.setForeground(new java.awt.Color(51, 102, 255));
        btnadd.setText("ADD");
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });
        jPanel1.add(btnadd, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 393, 111, 45));

        btnupdate.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnupdate.setForeground(new java.awt.Color(51, 102, 255));
        btnupdate.setText("Update");
        jPanel1.add(btnupdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 390, 111, 45));

        btnremove.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnremove.setForeground(new java.awt.Color(255, 0, 51));
        btnremove.setText("Remove");
        jPanel1.add(btnremove, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 111, 47));

        btnview.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnview.setForeground(new java.awt.Color(51, 102, 255));
        btnview.setText("View");
        btnview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnviewActionPerformed(evt);
            }
        });
        jPanel1.add(btnview, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 450, 111, 47));

        btnclear.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnclear.setText("Clear");
        btnclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnclearActionPerformed(evt);
            }
        });
        jPanel1.add(btnclear, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 40, -1, 30));

        cmbcourse.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        cmbcourse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "__", "Software Engineering", "Computer Science", "Business Management", "Agri" }));
        jPanel1.add(cmbcourse, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 180, 34));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(204, 204, 255));
        jLabel14.setText("Course");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 71, 34));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jScrollPane5.setViewportView(jScrollPane1);

        jPanel1.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 760, 420));

        btnreportview.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnreportview.setForeground(new java.awt.Color(255, 51, 51));
        btnreportview.setText("View Report");
        btnreportview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnreportviewActionPerformed(evt);
            }
        });
        jPanel1.add(btnreportview, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, -1, 30));

        jButton2.setBackground(new java.awt.Color(255, 0, 0));
        jButton2.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Log Out");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 10, 80, 30));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/geometric-1732847_1280.jpg"))); // NOI18N
        jLabel18.setText("jLabel18");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1330, 570));

        jTabbedPane1.addTab("Manage Students", jPanel1);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 204, 255));
        jLabel8.setText("Student ID");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 55, -1, 20));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 204, 255));
        jLabel9.setText("Subject 1 ");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 135, -1, 20));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 204, 255));
        jLabel10.setText("Subject 2");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, -1, -1));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 204, 255));
        jLabel11.setText("Subject 3");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 215, -1, 20));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 204, 255));
        jLabel12.setText("Subject 4");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, -1, -1));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(204, 204, 255));
        jLabel13.setText("Subject 5");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 310, -1, -1));

        btnAddMark.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnAddMark.setForeground(new java.awt.Color(51, 153, 255));
        btnAddMark.setText("Add");
        btnAddMark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMarkActionPerformed(evt);
            }
        });
        jPanel3.add(btnAddMark, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 380, 90, 30));

        btnmarkupdate.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnmarkupdate.setForeground(new java.awt.Color(51, 153, 255));
        btnmarkupdate.setText("Update");
        jPanel3.add(btnmarkupdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 380, 80, 31));

        btnmarkdelete.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnmarkdelete.setForeground(new java.awt.Color(51, 153, 255));
        btnmarkdelete.setText("Delete");
        jPanel3.add(btnmarkdelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 430, 90, 31));

        btnmarkclear.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnmarkclear.setForeground(new java.awt.Color(255, 0, 51));
        btnmarkclear.setText("Clear");
        btnmarkclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmarkclearActionPerformed(evt);
            }
        });
        jPanel3.add(btnmarkclear, new org.netbeans.lib.awtextra.AbsoluteConstraints(302, 430, 80, 31));

        cmbStudentId.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        cmbStudentId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "__" }));
        jPanel3.add(cmbStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 51, 206, 30));

        txtsubject1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jPanel3.add(txtsubject1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 206, 30));

        txtsubject2.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtsubject2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsubject2ActionPerformed(evt);
            }
        });
        jPanel3.add(txtsubject2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, 206, 30));

        txtsubject3.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jPanel3.add(txtsubject3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 210, 206, 30));

        txtsubject4.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtsubject4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsubject4ActionPerformed(evt);
            }
        });
        jPanel3.add(txtsubject4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 250, 206, 30));

        txtsubject5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jPanel3.add(txtsubject5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 290, 206, 30));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(204, 204, 255));
        jLabel15.setText("Name");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, -1, -1));

        txtStudentName.setEditable(false);
        txtStudentName.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jPanel3.add(txtStudentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 206, 30));

        btnmarkview.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnmarkview.setForeground(new java.awt.Color(0, 102, 255));
        btnmarkview.setText("Search Student marks");
        jPanel3.add(btnmarkview, new org.netbeans.lib.awtextra.AbsoluteConstraints(707, 48, 185, 32));

        markstable.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        markstable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(markstable);

        jScrollPane3.setViewportView(jScrollPane2);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(454, 98, 687, 373));

        btnViewAllMarks.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnViewAllMarks.setForeground(new java.awt.Color(0, 102, 255));
        btnViewAllMarks.setText("View");
        jPanel3.add(btnViewAllMarks, new org.netbeans.lib.awtextra.AbsoluteConstraints(542, 47, 117, 32));

        viewacareport.setForeground(new java.awt.Color(255, 0, 51));
        viewacareport.setText("View report");
        viewacareport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewacareportActionPerformed(evt);
            }
        });
        jPanel3.add(viewacareport, new org.netbeans.lib.awtextra.AbsoluteConstraints(1039, 489, -1, -1));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/geometric-1732847_1280.jpg"))); // NOI18N
        jLabel19.setText("jLabel19");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1330, 570));

        jTabbedPane1.addTab("Acadamic Data", jPanel3);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel4.add(txtStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 28, 163, 44));

        btnAnalyze.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnAnalyze.setForeground(new java.awt.Color(51, 153, 255));
        btnAnalyze.setText("Analyze");
        btnAnalyze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalyzeActionPerformed(evt);
            }
        });
        jPanel4.add(btnAnalyze, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 39, 86, -1));
        jPanel4.add(gpaProgress, new org.netbeans.lib.awtextra.AbsoluteConstraints(655, 13, 501, 40));

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 575, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );

        jPanel4.add(chartPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(309, 96, -1, -1));
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(111, 134, -1, -1));

        lblName.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblName.setForeground(new java.awt.Color(255, 255, 255));
        lblName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 88, 240, 40));

        lblCourse.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblCourse.setForeground(new java.awt.Color(255, 255, 255));
        lblCourse.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.add(lblCourse, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 152, 240, 40));

        lblTotal.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(255, 255, 255));
        lblTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 218, 240, 40));

        lblAverage.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblAverage.setForeground(new java.awt.Color(255, 255, 255));
        lblAverage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.add(lblAverage, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 336, 240, 40));

        lblGPA.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lblGPA.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.add(lblGPA, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 13, 87, 40));

        lblGrade.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblGrade.setForeground(new java.awt.Color(255, 255, 255));
        lblGrade.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.add(lblGrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 402, 240, 40));

        lblRank.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblRank.setForeground(new java.awt.Color(255, 255, 255));
        lblRank.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.add(lblRank, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 278, 240, 40));

        lblSubjects.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblSubjects.setForeground(new java.awt.Color(255, 255, 255));
        lblSubjects.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.add(lblSubjects, new org.netbeans.lib.awtextra.AbsoluteConstraints(902, 113, 234, 40));

        lblMissing.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblMissing.setForeground(new java.awt.Color(255, 255, 255));
        lblMissing.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.add(lblMissing, new org.netbeans.lib.awtextra.AbsoluteConstraints(902, 165, 234, 40));

        lblFailed.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblFailed.setForeground(new java.awt.Color(255, 255, 255));
        lblFailed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.add(lblFailed, new org.netbeans.lib.awtextra.AbsoluteConstraints(902, 234, 234, 157));

        txtAdvice.setColumns(20);
        txtAdvice.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        txtAdvice.setRows(5);
        jScrollPane4.setViewportView(txtAdvice);

        jPanel4.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(902, 417, -1, 71));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(204, 204, 255));
        jLabel17.setText("Student ID");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 28, 91, 44));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/geometric-1732847_1280.jpg"))); // NOI18N
        jLabel20.setText("jLabel20");
        jPanel4.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1330, 560));

        jTabbedPane1.addTab("Performance", jPanel4);

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 102, 255));
        jButton1.setText("Marks Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, 347, 110));

        btnanalyze.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnanalyze.setForeground(new java.awt.Color(0, 102, 255));
        btnanalyze.setText("Acadamic analyze");
        btnanalyze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnanalyzeActionPerformed(evt);
            }
        });
        jPanel5.add(btnanalyze, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 310, 347, 110));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/geometric-1732847_1280.jpg"))); // NOI18N
        jLabel21.setText("jLabel21");
        jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1330, 570));

        jTabbedPane1.addTab("Reports", jPanel5);

        lblRank1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 566, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(557, 557, 557)
                    .addComponent(lblRank1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1093, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(286, 286, 286)
                    .addComponent(lblRank1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(274, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDActionPerformed

    private void btnclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnclearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnclearActionPerformed

    private void txtnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnameActionPerformed

    private void btnviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnviewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnviewActionPerformed

    private void btnAnalyzeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalyzeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAnalyzeActionPerformed

    private void btnreportviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnreportviewActionPerformed
        try {
    String reportPath = "C:\\Users\\User\\JaspersoftWorkspace\\projectEADcw\\Blank_A4.jrxml";
    JasperReport report = JasperCompileManager.compileReport(reportPath);
    Connection conn = DatabaseManager.getConnection(); // from your model
    JasperPrint print = JasperFillManager.fillReport(report, null, conn);
    JasperViewer.viewReport(print, false);

} catch (Exception ex) {
    ex.printStackTrace();
    JOptionPane.showMessageDialog(null, "Failed to load report: " + ex.getMessage());
}

    }//GEN-LAST:event_btnreportviewActionPerformed

    private void viewacareportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewacareportActionPerformed
        try {
    String reportPath = "C:\\Users\\User\\JaspersoftWorkspace\\projectEADcw\\Invoice.jrxml";
    JasperReport report = JasperCompileManager.compileReport(reportPath);
    Connection conn = DatabaseManager.getConnection(); // from your model
    JasperPrint print = JasperFillManager.fillReport(report, null, conn);
    JasperViewer.viewReport(print, false);

} catch (Exception ex) {
    ex.printStackTrace();
    JOptionPane.showMessageDialog(null, "Failed to load report: " + ex.getMessage());
}
    }//GEN-LAST:event_viewacareportActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
    String reportPath = "C:\\Users\\User\\JaspersoftWorkspace\\projectEADcw\\Invoice.jrxml";
    JasperReport report = JasperCompileManager.compileReport(reportPath);
    Connection conn = DatabaseManager.getConnection(); // from your model
    JasperPrint print = JasperFillManager.fillReport(report, null, conn);
    JasperViewer.viewReport(print, false);

} catch (Exception ex) {
    ex.printStackTrace();
    JOptionPane.showMessageDialog(null, "Failed to load report: " + ex.getMessage());
}

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnanalyzeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnanalyzeActionPerformed
         try {
    String reportPath = "C:\\Users\\User\\JaspersoftWorkspace\\projectEADcw\\students_performance_report.jrxml";
    JasperReport report = JasperCompileManager.compileReport(reportPath);
    Connection conn = DatabaseManager.getConnection();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("student_id", loggedInUserId); 

JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);
    JasperViewer.viewReport(print, false);

} catch (Exception ex) {
    ex.printStackTrace();
    JOptionPane.showMessageDialog(null, "Failed to load report: " + ex.getMessage());
}
    }//GEN-LAST:event_btnanalyzeActionPerformed

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        
    }//GEN-LAST:event_btnaddActionPerformed

    private void btnmarkclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmarkclearActionPerformed
        txtStudentName.setText("");
        cmbStudentId.setSelectedIndex(-1);
        txtsubject1.setText("");
        txtsubject2.setText("");
        txtsubject3.setText("");
        txtsubject4.setText("");
        txtsubject5.setText("");
        
    }//GEN-LAST:event_btnmarkclearActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        login lg = new login();
        lg.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnAddMarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMarkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddMarkActionPerformed

    private void txtsubject2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsubject2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsubject2ActionPerformed

    private void txtsubject4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsubject4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsubject4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatLightLaf());

        java.awt.EventQueue.invokeLater(() -> {
        Stdmanagement view = new Stdmanagement();
        
        new StudentController(new StudentDAO(), view); // 
        view.setVisible(true);
    });

       
       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddMark;
    private javax.swing.JButton btnAnalyze;
    private javax.swing.JButton btnViewAllMarks;
    public javax.swing.JButton btnadd;
    private javax.swing.JButton btnanalyze;
    private javax.swing.JButton btnclear;
    private javax.swing.JButton btnmarkclear;
    private javax.swing.JButton btnmarkdelete;
    private javax.swing.JButton btnmarkupdate;
    private javax.swing.JButton btnmarkview;
    public javax.swing.JButton btnremove;
    private javax.swing.JButton btnreportview;
    public javax.swing.JButton btnupdate;
    public javax.swing.JButton btnview;
    private javax.swing.JPanel chartPanel;
    private javax.swing.JComboBox<String> cmbStudentId;
    private javax.swing.JComboBox<String> cmbcourse;
    private javax.swing.JProgressBar gpaProgress;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    public javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAverage;
    private javax.swing.JLabel lblCourse;
    private javax.swing.JLabel lblFailed;
    private javax.swing.JLabel lblGPA;
    private javax.swing.JLabel lblGrade;
    private javax.swing.JLabel lblMissing;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblRank;
    private javax.swing.JLabel lblRank1;
    private javax.swing.JLabel lblSubjects;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable markstable;
    private javax.swing.JTextArea txtAdvice;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtStudentId;
    private javax.swing.JTextField txtStudentName;
    private javax.swing.JTextField txtaddress;
    private javax.swing.JTextField txtbirth;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtname;
    private javax.swing.JTextField txtnumber;
    private javax.swing.JTextField txtsubject1;
    private javax.swing.JTextField txtsubject2;
    private javax.swing.JTextField txtsubject3;
    private javax.swing.JTextField txtsubject4;
    private javax.swing.JTextField txtsubject5;
    private javax.swing.JButton viewacareport;
    // End of variables declaration//GEN-END:variables

   
}
