package ai_app;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.net.*;
import java.io.*;
import org.json.*;
import java.util.Properties;
import java.io.InputStream;


public class AISQLAssistant extends JFrame {

    JTextArea inputArea, sqlArea, terminalArea;
    JTable table;
    JButton btnAI, btnView;
    String createdBy;
    
 // NOTE: API key is intentionally not included for security reasons.
 // Add your own Gemini API key here to enable AI functionality.
    private static final String API_KEY = "YOUR_API_KEY";


    public AISQLAssistant(String createdBy) {

        this.createdBy = createdBy;

        setTitle("AI SQL Assistant - Welcome " + createdBy);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("🤖 AI SQL ASSISTANT");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel userLbl = new JLabel("Logged in as: " + createdBy);
        userLbl.setForeground(Color.LIGHT_GRAY);

        header.add(title, BorderLayout.WEST);
        header.add(userLbl, BorderLayout.EAST);

        getContentPane().add(header, BorderLayout.NORTH);

        // ================= LEFT PANEL =================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        leftPanel.setBackground(Color.WHITE);

        JLabel lblInput = new JLabel("Natural Language Instruction:");
        lblInput.setFont(new Font("Segoe UI", Font.BOLD, 13));

        inputArea = new JTextArea(5, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBorder(new LineBorder(Color.GRAY));

        JScrollPane inputScroll = new JScrollPane(inputArea);

        leftPanel.add(lblInput);
        leftPanel.add(inputScroll);
        leftPanel.add(Box.createVerticalStrut(15));

        // ===== ACTION CARDS =====
        leftPanel.add(createActionPanel());
        leftPanel.add(Box.createVerticalStrut(15));

        JLabel lblSQL = new JLabel("Generated SQL Query:");
        lblSQL.setFont(new Font("Segoe UI", Font.BOLD, 13));

        sqlArea = new JTextArea(4, 40);
        sqlArea.setEditable(false);
        sqlArea.setBorder(new LineBorder(Color.GRAY));
        sqlArea.setBackground(new Color(245, 245, 245));

        JScrollPane sqlScroll = new JScrollPane(sqlArea);

        leftPanel.add(lblSQL);
        leftPanel.add(sqlScroll);

        // ================= RIGHT PANEL (TABLE) =================
        table = new JTable();
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("User Data"));

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftPanel,
                tableScroll
        );
        splitPane.setResizeWeight(0.45);

        getContentPane().add(splitPane, BorderLayout.CENTER);

        // ================= TERMINAL =================
        terminalArea = new JTextArea(6, 100);
        terminalArea.setEditable(false);
        terminalArea.setBackground(Color.WHITE);
        terminalArea.setForeground(Color.BLUE);
        terminalArea.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane terminalScroll = new JScrollPane(terminalArea);
        terminalScroll.setBorder(BorderFactory.createTitledBorder("Terminal"));

        getContentPane().add(terminalScroll, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        btnAI.addActionListener(e -> executeAI());
        btnView.addActionListener(e -> loadMyData());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ================= ACTION PANEL =================
    private JPanel createActionPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBackground(Color.WHITE);

        btnAI = createCard("🤖", "Execute with AI",
                "Convert instruction into SQL\nand execute",
                new Color(52, 152, 219));

        btnView = createCard("🔄", "Refresh Table",
                "Reload latest data\nfrom database",
                new Color(46, 204, 113));

        panel.add(btnAI.getParent());
        panel.add(btnView.getParent());

        return panel;
    }

    private JButton createCard(String icon, String title, String desc, Color color) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
                new LineBorder(color, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(250, 250, 250));

        JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        iconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLbl = new JLabel("<html><center>" + desc.replace("\n", "<br>") + "</center></html>");
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLbl.setForeground(Color.DARK_GRAY);
        descLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btn = new JButton(title);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLbl);
        card.add(Box.createVerticalStrut(5));
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(5));
        card.add(descLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(btn);

        btn.putClientProperty("card", card);
        return btn;
    }

    // ================= LOGIC =================
    private void executeAI() {
        try {
            terminal("Instruction received");

            String instruction = inputArea.getText().trim();
            if (instruction.isEmpty()) {
                terminal("Please enter an instruction");
                return;
            }

            String prompt =
                    "Convert instruction into MySQL query. " +
                    "Table: userdetails(name, age, gender, city, dob, CreatedBy). " +
                    "Always use CreatedBy='" + createdBy + "'. " +
                    "Instruction: " + instruction;

            terminal("Calling AI...");
            String sql = callGemini(prompt)
                    .replace("```sql", "")
                    .replace("```", "")
                    .trim();

            sqlArea.setText(sql);

            if (isDangerous(sql)) {
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "This query may modify data.\nDo you want to continue?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION
                );
                if (choice != JOptionPane.YES_OPTION) {
                    terminal("Execution cancelled");
                    return;
                }
            }

            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            st.execute(sql);

            terminal("Query executed successfully");
            loadMyData();

        } catch (Exception e) {
            terminal("ERROR: " + e.getMessage());
        }
    }

    private boolean isDangerous(String sql) {
        String s = sql.toLowerCase();
        return s.contains("delete") || s.contains("drop")
                || s.contains("truncate") || s.contains("alter")
                || s.contains("update");
    }

    private void loadMyData() {
        try {
            terminal("Loading user data...");
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM userdetails WHERE CreatedBy=?"
            );
            ps.setString(1, createdBy);

            ResultSet rs = ps.executeQuery();
            showInTable(rs);
            terminal("Data loaded successfully");

        } catch (Exception e) {
            terminal("ERROR loading data");
        }
    }

    private void showInTable(ResultSet rs) throws Exception {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();

        DefaultTableModel model = new DefaultTableModel();
        for (int i = 1; i <= cols; i++) {
            model.addColumn(meta.getColumnName(i));
        }

        while (rs.next()) {
            Object[] row = new Object[cols];
            for (int i = 1; i <= cols; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }
        table.setModel(model);
    }

    private void terminal(String msg) {
        terminalArea.append(">> " + msg + "\n");
    }

    private String callGemini(String prompt) throws Exception {

        URL url = new URL(
                "https://generativelanguage.googleapis.com/v1beta/models/" +
                        "gemini-3-flash-preview:generateContent"
        );

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("x-goog-api-key", API_KEY);
        con.setDoOutput(true);

        String body =
                "{ \"contents\": [ { \"parts\": [ { \"text\": \"" +
                        prompt.replace("\"", "\\\"") +
                        "\" } ] } ] }";

        OutputStream os = con.getOutputStream();
        os.write(body.getBytes());
        os.close();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );

        StringBuilder res = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) res.append(line);

        JSONObject obj = new JSONObject(res.toString());
        return obj.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");
    }
    public static void main(String[] args) {
        new Registration();
    }
}
