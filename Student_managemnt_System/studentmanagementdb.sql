CREATE DATABASE IF NOT EXISTS Stdmanagement;
USE Stdmanagement;

CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    dob VARCHAR(20),
    address TEXT,
    course VARCHAR(100)
);
CREATE TABLE marks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(255),
    student_name VARCHAR(255),
    subject1 INT,
    subject2 INT,
    subject3 INT,
    subject4 INT,
    subject5 INT,
    FOREIGN KEY (student_id) REFERENCES students(id)
);
CREATE TABLE admin (
    admin_id VARCHAR(50) PRIMARY KEY,
    admin_name VARCHAR(100),
    password VARCHAR(100)
);
INSERT INTO admin (admin_id, admin_name, password) VALUES
('admin01', 'kasun', 'adminkasun123'),
('admin02', 'dasun', 'dasu123'),
('admin03', 'pawan', 'p1123');


SELECT * FROM students
SELECT * FROM marks;
SELECT * FROM admin;