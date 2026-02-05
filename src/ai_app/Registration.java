package ai_app;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Registration extends JFrame {

    JTextField tfUserId, tfName;
    JPasswordField tfPassword;
    JButton btnRegister, btnBack;

    public Registration() {

        setTitle("Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        getContentPane().setLayout(new BorderLayout());

        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topWrapper.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Register"));
        GridBagLayout gbl_formPanel = new GridBagLayout();
        gbl_formPanel.columnWidths = new int[]{48, 30, 150, 0};
        gbl_formPanel.rowHeights = new int[]{19, 19, 19, 30, 31, 0};
        gbl_formPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_formPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        formPanel.setLayout(gbl_formPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel label_2 = new JLabel("User ID:");
        formPanel.add(label_2, gbc);
        tfUserId = new JTextField(18);
        GridBagConstraints gbc_tfUserId = new GridBagConstraints();
        gbc_tfUserId.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfUserId.insets = new Insets(0, 0, 5, 0);
        gbc_tfUserId.gridx = 2;
        gbc_tfUserId.gridy = 0;
        formPanel.add(tfUserId, gbc_tfUserId);
        GridBagConstraints gbc_1 = new GridBagConstraints();
        gbc_1.anchor = GridBagConstraints.EAST;
        gbc_1.insets = new Insets(0, 0, 5, 5);
        gbc_1.gridx = 0;
        gbc_1.gridy = 1;
        JLabel label_1 = new JLabel("Name:");
        formPanel.add(label_1, gbc_1);
        tfName = new JTextField(18);
        GridBagConstraints gbc_tfName = new GridBagConstraints();
        gbc_tfName.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfName.insets = new Insets(0, 0, 5, 0);
        gbc_tfName.gridx = 2;
        gbc_tfName.gridy = 1;
        formPanel.add(tfName, gbc_tfName);
        GridBagConstraints gbc_2 = new GridBagConstraints();
        gbc_2.anchor = GridBagConstraints.EAST;
        gbc_2.insets = new Insets(0, 0, 5, 5);
        gbc_2.gridx = 0;
        gbc_2.gridy = 2;
        JLabel label = new JLabel("Password:");
        formPanel.add(label, gbc_2);
        tfPassword = new JPasswordField(18);
        GridBagConstraints gbc_tfPassword = new GridBagConstraints();
        gbc_tfPassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfPassword.insets = new Insets(0, 0, 5, 0);
        gbc_tfPassword.gridx = 2;
        gbc_tfPassword.gridy = 2;
        formPanel.add(tfPassword, gbc_tfPassword);

        topWrapper.add(formPanel);
        
               
                JPanel buttonPanel = new JPanel();
                btnRegister = new JButton("Register");
                btnBack = new JButton("Back to Login");
                buttonPanel.add(btnRegister);
                buttonPanel.add(btnBack);
                GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
                gbc_buttonPanel.gridwidth = 3;
                gbc_buttonPanel.gridx = 0;
                gbc_buttonPanel.gridy = 4;
                formPanel.add(buttonPanel, gbc_buttonPanel);
                
                     
                        btnRegister.addActionListener(e -> registerUser());
                        btnBack.addActionListener(e -> {
                            dispose();
                            new Login();
                        });
        getContentPane().add(topWrapper, BorderLayout.NORTH);

        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void registerUser() {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO users(userid, name, password) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tfUserId.getText());
            ps.setString(2, tfName.getText());
            ps.setString(3, new String(tfPassword.getPassword()));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Registration Successful");
                dispose();
                new Login();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during registration");
        }
    }

    public static void main(String[] args) {
        new Registration();
    }
}
