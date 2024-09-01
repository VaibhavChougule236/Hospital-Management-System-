
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class HospitalManagement extends JFrame {
    JFrame frame;
    DefaultTableModel tableModel;
    JTable table;

    JPanel buttonPanel;
    JPanel operationPanel;
    HospitalManagement() {
        setTitle("Hospital Management");
        buttonPanel = new JPanel(new GridLayout(9, 1, 10, 10));
        buttonPanel.setBackground(Color.CYAN);

        operationPanel = new JPanel(new BorderLayout());
        operationPanel.setBorder(BorderFactory.createTitledBorder("View"));

        
        JLabel label = new JLabel("Services ");
        label.setFont(new Font("Arial",20,20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(Color.ORANGE);
        label.setPreferredSize(new Dimension(780, 50));
        buttonPanel.add(label);

        
        JButton b1 = new JButton("Add Patient");
        JButton b2 = new JButton("View Patients");
        JButton b3 = new JButton("Check Patient");
        JButton b8 = new JButton("Delete Patient");
        JButton b4 = new JButton("View Doctors");
        JButton b5 = new JButton("Check Doctor");
        JButton b6 = new JButton("Book Appointment");
        JButton b7 = new JButton("Exit");

        
        buttonPanel.add(b1);
        buttonPanel.add(b2);
        buttonPanel.add(b3);
        buttonPanel.add(b8);
        buttonPanel.add(b4);
        buttonPanel.add(b5);
        buttonPanel.add(b6);
        buttonPanel.add(b7);

       
        setLayout(new GridLayout(1, 2));
        add(buttonPanel); 
        add(operationPanel); 

        
        operationPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Operations"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        
        JLabel operationLabel = new JLabel("Select a service to perform operations.");
        operationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        operationPanel.add(operationLabel, BorderLayout.CENTER);

       
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewPatients();
            }
        });

        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int Id = Integer.parseInt(JOptionPane.showInputDialog(operationPanel, "Enter Patient Id:"));
                String Name = JOptionPane.showInputDialog(operationPanel, "Enter Patient Name:");
                CheckPatient(Id, Name);
            }
        });

        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewDoctors();
            }
        });

        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Name = JOptionPane.showInputDialog(operationPanel, "Enter Doctor Name:");
                CheckDoctor(Name);
            }
        });

        b6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookAppointment();
            }
        });

        b7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Exist();
            }
        });
        b8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int Patient_Id = Integer.parseInt(JOptionPane.showInputDialog(operationPanel, "Enter Patient ID :"));
                String Patient_Name = JOptionPane.showInputDialog(operationPanel, "Enter Name:");
                deletePatient(Patient_Id,Patient_Name);
            }
        });
    }


public void deletePatient(int Patient_Id,String Patient_Name){
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "Vbcd@123");
        String query = "DELETE FROM patient WHERE Id= ? AND Name=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, Patient_Id);
        statement.setString(2, Patient_Name);
        int AffectedRows = statement.executeUpdate();
        if (AffectedRows > 0) {
            JOptionPane.showMessageDialog(operationPanel, "Patient Deleted Successfully");
        } else {
            JOptionPane.showMessageDialog(operationPanel, "Fail to Delete patient");
        }
    }catch (Exception a){
        a.printStackTrace();
    }
}
    private String[][] getDataOfPatientsFromDatabase() {
        String[][] data = null;
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
           
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "Vbcd@123");
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);


            rs = statement.executeQuery("SELECT * FROM Patient");
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            data = new String[rowCount][5];

            int row = 0;
            while (rs.next()) {
                data[row][0] = rs.getString("Id");
                data[row][1] = rs.getString("Name");
                data[row][2] = rs.getString("Age");
                data[row][3] = rs.getString("Gender");
                data[row][4] = rs.getString("Mob_No");
                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private String[][] getDataOfDoctorsFromDataBases() {
        String[][] Data = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "Vbcd@123");
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "SELECT * FROM Doctors";
            rs = statement.executeQuery(query);
            rs.last();
            int rows = rs.getRow();
            rs.beforeFirst();
            Data = new String[rows][3]; 
            int row = 0;
            while (rs.next()) {
                Data[row][0] = rs.getString("Id");
                Data[row][1] = rs.getString("Name");
                Data[row][2] = rs.getString("Specialization");
                row++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Data;
    }

    public void ViewPatients() {
        JFrame jp = new JFrame();
        String[] ColumnNames = {"Id", "Name", "Age", "Gender", "Mob_No"};
        tableModel = new DefaultTableModel(getDataOfPatientsFromDatabase(), ColumnNames);
        table = new JTable(tableModel);
        jp.setSize(500, 300);
        jp.add(new JScrollPane(table)); 
       jp.setLocationRelativeTo(null);
        jp.setVisible(true);
    }

    public void ViewDoctors() {
        JFrame j = new JFrame("Data");
        String[] ColumnNames = {"Id", "Name", "Specialization"};
        tableModel = new DefaultTableModel(getDataOfDoctorsFromDataBases(), ColumnNames);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        j.setSize(500, 300);
        j.add(scrollPane, BorderLayout.CENTER);
        j.setLocationRelativeTo(null);
        j.setVisible(true);
    }

    public void addPatient() {
        String Name = JOptionPane.showInputDialog(operationPanel, "Enter Name:");
        int Age = Integer.parseInt(JOptionPane.showInputDialog(operationPanel, "Enter Age:"));
        String Gender = JOptionPane.showInputDialog(operationPanel, "Enter Gender (M/F):");
        String MobileNo = JOptionPane.showInputDialog(operationPanel, "Enter MobileNo:");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "Vbcd@123");
            String query = "INSERT INTO patient(Name, Age, Gender, Mob_No) VALUES(?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, Name);
            statement.setInt(2, Age);
            statement.setString(3, Gender);
            statement.setString(4, MobileNo);
            int AffectedRows = statement.executeUpdate();
            if (AffectedRows > 0) {
                JOptionPane.showMessageDialog(operationPanel, "Patient Added Successfully");
            } else {
                JOptionPane.showMessageDialog(operationPanel, "Fail to add patient");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CheckPatient(int Id, String Name) {
        String query = "SELECT * FROM Patient WHERE Id = ? AND Name = ?";
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "Vbcd@123");
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, Id);
            statement.setString(2, Name);
            ResultSet Rs = statement.executeQuery();
            if (Rs.next()) {
                JOptionPane.showMessageDialog(operationPanel, "The Patient is Present");
            } else {
                JOptionPane.showMessageDialog(operationPanel, "The Patient is not Present");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CheckDoctor(String Name) {
        String query = "SELECT * FROM doctors WHERE Name=?";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "Vbcd@123");
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, Name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(operationPanel, "Doctor is Present.");
            } else {
                JOptionPane.showMessageDialog(operationPanel, "Doctor is not Present.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bookAppointment() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "Vbcd@123")) {
            int patientId = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Patient ID:"));
            int doctorId = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Doctor ID:"));
            String appointmentDate = JOptionPane.showInputDialog(frame, "Enter Appointment Date (YYYY-MM-DD):");
            LocalDate date = LocalDate.parse(appointmentDate);

            if (checkPatient(conn, patientId) && checkDoctor(conn, doctorId)) {
                if (doctorIsAvailable(conn, doctorId, appointmentDate)) {
                   
                    String query = "INSERT INTO Appointments(Patient_Id, Doctor_Id, Appointment_Date) VALUES(?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, patientId);
                    statement.setInt(2, doctorId);
                    statement.setDate(3, Date.valueOf(date));
                    int affectedRows = statement.executeUpdate();
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(frame, "Appointment Added Successfully.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to Add Appointment.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Doctor not available on this date.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Patient or Doctor ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPatient(Connection connection, int patientId) {
        String query = "SELECT COUNT(*) FROM Patient WHERE Id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, patientId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkDoctor(Connection connection, int doctorId) {
        String query = "SELECT COUNT(*) FROM Doctors WHERE Id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, doctorId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean doctorIsAvailable(Connection connection, int doctorId, String appointmentDate) {
        String query = "SELECT COUNT(*) FROM Appointments WHERE Doctor_Id = ? AND Appointment_Date = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, doctorId);
            statement.setString(2, appointmentDate);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void Exist() {
        int a = JOptionPane.showConfirmDialog(operationPanel, "Are you sure?");
        if (a == JOptionPane.YES_OPTION) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public static void main(String[] args) {
        new HospitalManagement();
    }
}
