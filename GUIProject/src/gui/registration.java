package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class registration extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField nameField, mobileField, addressField;
    private JComboBox<String> genderComboBox;
    private JCheckBox termsCheckBox;
    private JTable table;

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/app1";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    registration frame = new registration();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public registration() {
        // Set up frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Add input fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Gender:"));
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        inputPanel.add(genderComboBox);

        inputPanel.add(new JLabel("Mobile:"));
        mobileField = new JTextField();
        inputPanel.add(mobileField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        termsCheckBox = new JCheckBox("I accept the terms and conditions");
        inputPanel.add(termsCheckBox);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertData();
            }
        });
        inputPanel.add(submitButton);

        contentPane.add(inputPanel, BorderLayout.NORTH);

        // Add table to display data
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Load data into the table on startup
        loadData();
    }

    /**
     * Insert data into the database.
     */
    private void insertData() {
        String name = nameField.getText();
        String gender = (String) genderComboBox.getSelectedItem();
        String mobile = mobileField.getText();
        String address = addressField.getText();
        boolean termsAccepted = termsCheckBox.isSelected();

        if (name.isEmpty() || mobile.isEmpty() || address.isEmpty() || !termsAccepted) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and accept the terms!");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO users (Name, Gender, Mobile, Address, Terms) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, gender);
            statement.setString(3, mobile);
            statement.setString(4, address);
            statement.setBoolean(5, termsAccepted);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Data inserted successfully!");
                loadData(); // Refresh table data
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inserting data: " + ex.getMessage());
        }
    }

    /**
     * Load data from the database into the table.
     */
    private void loadData() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Use DbUtils to convert the ResultSet to a TableModel and display it in the JTable
            table.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }
}
