import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SQLQuery extends JFrame {
    private final String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=Attendance;encrypt=true;trustServerCertificate=true";
    private final String dbUser = "sa";
    private final String dbPassword = "Huynhthien123";
    DefaultTableModel tableModel = new DefaultTableModel();
    JTextField queryText = new JTextField();

    private JFrame frame;
    private JTextField studentNameField;
    private JTextField subjectNameField;
    private JTextField teacherNameField;
    private JTextField lessonDateField;
    private JTextField teacherIDField;
    private JTextField subjectIDField;

    public SQLQuery() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("University Platform Management");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblStudentName = new JLabel("Student's Name");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        frame.getContentPane().add(lblStudentName, gbc);

        studentNameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        frame.getContentPane().add(studentNameField, gbc);
        studentNameField.setColumns(10);

        JButton btnQueryStudentId = new JButton("QUERY STUDENT'S ID");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        frame.getContentPane().add(btnQueryStudentId, gbc);

        JButton btnAddStudent = new JButton("ADD STUDENT");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        frame.getContentPane().add(btnAddStudent, gbc);

        JButton btnDeleteAttendanceRecord = new JButton("DELETE ATTENDANCE RECORD");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        frame.getContentPane().add(btnDeleteAttendanceRecord, gbc);

        JLabel lblSubjectName = new JLabel("Subject's Name");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        frame.getContentPane().add(lblSubjectName, gbc);

        subjectNameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        frame.getContentPane().add(subjectNameField, gbc);
        subjectNameField.setColumns(10);

        JLabel lblTeacherName = new JLabel("Teacher's Name");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        frame.getContentPane().add(lblTeacherName, gbc);

        teacherNameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        frame.getContentPane().add(teacherNameField, gbc);
        teacherNameField.setColumns(10);

        JLabel lblLessonDate = new JLabel("Date of Lesson (YYYY-MM-DD)");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        frame.getContentPane().add(lblLessonDate, gbc);

        lessonDateField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        frame.getContentPane().add(lessonDateField, gbc);
        lessonDateField.setColumns(10);

        JButton btnAddSubject = new JButton("ADD SUBJECT");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        frame.getContentPane().add(btnAddSubject, gbc);

        JButton btnAddLesson = new JButton("ADD LESSON");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        frame.getContentPane().add(btnAddLesson, gbc);

        frame.setVisible(true);

        // Add action listener to the query button
        btnQueryStudentId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = studentNameField.getText();
                if (!studentName.isEmpty()) {
                    queryStudentName(studentName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a student's name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAddStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = studentNameField.getText();
                if (!studentName.isEmpty()) {
                    addStudent(studentName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a student's name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDeleteAttendanceRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = studentNameField.getText();
                if (!studentName.isEmpty()) {
                    deleteAttendanceRecord(studentName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a student's name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAddSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subjectName = subjectNameField.getText();
                if (!subjectName.isEmpty()) {
                    addSubject(subjectName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a subject's name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAddLesson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateOfLesson = lessonDateField.getText();
                String teacherName = teacherNameField.getText();
                String subjectName = subjectNameField.getText();
                if (!dateOfLesson.isEmpty() && !teacherName.isEmpty() && !subjectName.isEmpty()) {
                    addLesson(dateOfLesson, teacherName, subjectName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void addStudent(String studentName) {
        String query = "INSERT INTO Student (StudentName) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, studentName);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Student added successfully.", "Add Student", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add student.", "Add Student", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void queryStudentName(String name) {
        String query = "SELECT * FROM Student WHERE StudentName = '" + name + "'";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(frame, "Student found: " + resultSet.getString("StudentName"), "Query Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Student not found.", "Query Result", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAttendanceRecord(String studentName) {
        String query = "DELETE FROM Student WHERE StudentName = ?";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, studentName);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Attendance record deleted successfully.", "Delete Attendance Record", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "No attendance record found for the given student name.", "Delete Attendance Record", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSubject(String subjectName) {
        String query = "INSERT INTO Subject (SubjectName) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, subjectName);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Subject added successfully.", "Add Subject", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add subject.", "Add Subject", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getTeacherID(String teacherName) throws SQLException {
        String query = "SELECT TeacherID FROM Teacher WHERE TeacherName = ?";
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, teacherName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TeacherID");
                } else {
                    throw new SQLException("Teacher not found");
                }
            }
        }
    }

    private int getSubjectID(String subjectName) throws SQLException {
        String query = "SELECT SubjectID FROM Subject WHERE SubjectName = ?";
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, subjectName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("SubjectID");
                } else {
                    throw new SQLException("Subject not found");
                }
            }
        }
    }

    private void addLesson(String dateOfLesson, String teacherName, String subjectName) {
        try {
            int teaID = getTeacherID(teacherName);
            int subID = getSubjectID(subjectName);

            String query = "INSERT INTO Lesson (DateOfLesson, TeaID, SubID) VALUES (?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setDate(1, Date.valueOf(dateOfLesson));
                preparedStatement.setInt(2, teaID);
                preparedStatement.setInt(3, subID);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Lesson added successfully.", "Add Lesson", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add lesson.", "Add Lesson", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SQLQuery::new);
    }
}