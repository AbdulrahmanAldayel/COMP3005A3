//COMP 3005 Assignment 3(1)
//Abdulrahman Aldayel 101142760

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class StudentManagementApp {
    //database connection details
    private static final String URL = "jdbc:postgresql://localhost:5432/Assignment3";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    //restablish database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    //get and print student records from database
    public static void getAllStudents() {
        String query = "SELECT * FROM students";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println(rs.getInt("student_id") + " - " +
                        rs.getString("first_name") + " " +
                        rs.getString("last_name") + ", Email: " +
                        rs.getString("email") + ", Enrollment Date: " +
                        rs.getDate("enrollment_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //add new student record to database
    public static void addStudent(String first_name, String last_name, String email, LocalDate enrollment_date) {
        String query = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, email);
            pstmt.setDate(4, java.sql.Date.valueOf(enrollment_date));
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update email address of existing student
    public static void updateStudentEmail(int student_id, String new_email) {
        String query = "UPDATE students SET email = ? WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, new_email);
            pstmt.setInt(2, student_id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student email updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //delete student record from database
    public static void deleteStudent(int student_id) {
        String query = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, student_id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getAllStudents();
        addStudent("Alice", "Wonderland", "alice.wonder@example.com", LocalDate.of(2023, 9, 1));
        updateStudentEmail(1, "new.alice.email@example.com");
        deleteStudent(2);
    }
}
