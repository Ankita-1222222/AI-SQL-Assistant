package ai_app;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {

    JTextField tfUserId;
    JPasswordField tfPassword;
    JButton btnLogin, btnRegister;

    public Login() {

        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topWrapper.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Login"));

       
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.insets = new Insets(10, 15, 10, 15);
        gbc1.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("User ID:"), gbc1);

       
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        gbc2.insets = new Insets(10, 15, 10, 15);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        tfUserId = new JTextField(18);
        formPanel.add(tfUserId, gbc2);

        // -------- Password Label --------
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 0;
        gbc3.gridy = 1;
        gbc3.insets = new Insets(10, 15, 10, 15);
        gbc3.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Password:"), gbc3);

    
        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.gridx = 1;
        gbc4.gridy = 1;
        gbc4.insets = new Insets(10, 15, 10, 15);
        gbc4.fill = GridBagConstraints.HORIZONTAL;
        tfPassword = new JPasswordField(18);
        formPanel.add(tfPassword, gbc4);

       
        JPanel buttonPanel = new JPanel();
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);

        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.gridx = 0;
        gbc5.gridy = 2;
        gbc5.gridwidth = 2;
        gbc5.insets = new Insets(20, 0, 0, 0);
        gbc5.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc5);

        topWrapper.add(formPanel);
        add(topWrapper, BorderLayout.NORTH);

        
        btnLogin.addActionListener(e -> loginUser());
        btnRegister.addActionListener(e -> {
            dispose();
            new Registration();
        });

        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void loginUser() {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE userid=? AND password=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tfUserId.getText());
            ps.setString(2, new String(tfPassword.getPassword()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                dispose();
                new AISQLAssistant(tfUserId.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Invalid User ID or Password");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
