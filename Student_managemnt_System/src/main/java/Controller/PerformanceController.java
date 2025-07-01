package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
import model.Mark;
import model.MarkDAO;
import model.StudentDAO;
import View.Stdmanagement;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.AbstractMap;
import java.util.Map;



public class PerformanceController {
     private final Stdmanagement view;
    private final StudentDAO studentDAO;
    private final MarkDAO markDAO;

    public PerformanceController(Stdmanagement view, StudentDAO studentDAO, MarkDAO markDAO) {
        this.view = view;
        this.markDAO = markDAO != null ? markDAO : new MarkDAO();
        this.studentDAO = studentDAO;

        view.getAnalyzeButton().addActionListener(e -> analyzePerformance());

    }

    private void analyzePerformance() {
        String studentId = view.getEnteredStudentId();
        if (studentId.isBlank()) {
            JOptionPane.showMessageDialog(null, "Please enter a student ID.");
            return;
        }

        Mark mark = markDAO.getMarkByStudentId(studentId);
        if (mark == null) {
            JOptionPane.showMessageDialog(null, "Student not found or has no marks.");
            return;
        }

        String name = studentDAO.getStudentNameById(studentId);
        String course = studentDAO.getStudentCourseById(studentId); 

        view.getLabelCourse().setText("Course: " + course);
        
        Integer[] scores = mark.getSubjectArray();
        String[] subjectNames = { "Subject1", "Subject2", "Subject3", "Subject4", "Subject5" };

        int total = 0, count = 0, missing = 0;
        java.util.List<String> failedSubjects = new java.util.ArrayList<>();

        for (int i = 0; i < scores.length; i++) {
            Integer s = scores[i];
            if (s != null) {
                total += s;
                count++;
                if (s < 30) failedSubjects.add(subjectNames[i]);
            } else {
                missing++;
            }
        }

        double average = count > 0 ? total / (double) count : 0;
        double gpa = Math.round((average / 25.0) * 100.0) / 100.0;
        String grade = gpa >= 3.5 ? "A" : gpa >= 3.0 ? "B" : gpa >= 2.0 ? "C" : "F";

        // === Update View ===
        view.getLabelName().setText("Name: " + name);
        view.getLabelTotal().setText("Total Marks: " + total);
        view.getLabelAverage().setText("Average: " + String.format("%.2f", average));
        JLabel gpaLabel = view.getLabelGPA();
        gpaLabel.setText("GPA: " + String.format("%.2f", gpa));

        if (gpa > 3.5) {
            gpaLabel.setForeground(Color.GREEN.darker());
        } else if (gpa >= 2.5) {
            gpaLabel.setForeground(Color.ORANGE.darker());
        } else {
            gpaLabel.setForeground(Color.RED.darker());
        }

        
        view.getLabelGrade().setText("Grade: " + grade);
        view.getLabelSubjects().setText("Subjects Counted: " + count);
        view.getLabelMissing().setText("Missing Subjects: " + missing);

        if (failedSubjects.isEmpty()) {
            view.getLabelFailed().setText("Failed Subjects: None");
        } else {
            StringBuilder failed = new StringBuilder("<html>Failed Subjects:<br>");
            for (String sub : failedSubjects) failed.append("– ").append(sub).append("<br>");
            failed.append("</html>");
            view.getLabelFailed().setText(failed.toString());
        }

        JProgressBar bar = view.getGpaProgress();
        bar.setMaximum(400);
        bar.setValue((int) (gpa * 100));
        bar.setString(gpa + " / 4.0");

        if (gpa >= 3.5) {
            bar.setForeground(Color.GREEN.darker());
            view.getAdviceArea().setText("Excellent performance across all subjects!");
        } else if (gpa >= 2.5) {
            bar.setForeground(Color.YELLOW.darker());
            view.getAdviceArea().setText("You're doing well! Keep it up!");
        } else {
            bar.setForeground(Color.RED.darker());
            view.getAdviceArea().setText("Work on improving your weak subjects.");
        }

        // === Chart ===
        JPanel chartPanel = view.getChartPanel();
        chartPanel.removeAll();

        org.jfree.data.category.DefaultCategoryDataset dataset = new org.jfree.data.category.DefaultCategoryDataset();
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] != null) {
                dataset.addValue(scores[i], "Marks", subjectNames[i]);
            }
        }

        org.jfree.chart.JFreeChart chart = org.jfree.chart.ChartFactory.createBarChart(
            "Subject-wise Performance", "Subjects", "Marks", dataset,
            org.jfree.chart.plot.PlotOrientation.VERTICAL, false, true, false
        );

        chart.getCategoryPlot().getRenderer().setSeriesPaint(0, new Color(255, 99, 132));
        org.jfree.chart.ChartPanel chartUI = new org.jfree.chart.ChartPanel(chart);

        chartUI.setPreferredSize(new Dimension(575, 371));
        chartPanel.setLayout(new BorderLayout());
        chartPanel.add(chartUI, BorderLayout.CENTER);
        chartPanel.validate();
        chartPanel.repaint();
        

        // === Rank Calculation ===
       String prefix = getCoursePrefix(course);
        List<Mark> allMarks = markDAO.getAllMarksByCoursePrefix(prefix);
        List<Map.Entry<String, Double>> ranked = new java.util.ArrayList<>();

        for (Mark m : allMarks) {
            double g = m.getGPA(); // assuming Mark has a GPA getter/calc
            ranked.add(new AbstractMap.SimpleEntry<>(m.getStudentId(), g));
        }

        ranked.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        for (int i = 0; i < ranked.size(); i++) {
            if (ranked.get(i).getKey().equalsIgnoreCase(studentId)) {
                view.getLabelRank().setText("Rank: " + (i + 1) + " / " + ranked.size());
                break;
            }
        }
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


}
