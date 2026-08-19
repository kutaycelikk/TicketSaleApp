import javax.swing.*;
import java.awt.*;
import java.util.List;

// Main class controls the user interface
public class Main {

    static AuthService authService = new AuthService();
    static TicketService ticketService = new TicketService();
    static JFrame frame;
    static JPanel contentPanel;
    static JButton btnToggleTheme;
    static boolean isDarkMode = false;
    static Color redAccent = new Color(220, 53, 69);

    public static void main(String[] args) {
        // Create main application window
        frame = new JFrame("KutayTickets \"easiest way to get tickets!\"");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Setup top header for theme toggle
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnToggleTheme = new JButton();
        btnToggleTheme.setFocusPainted(false);
        btnToggleTheme.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnToggleTheme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggleTheme.setOpaque(true);
        btnToggleTheme.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Handle theme switch action
        btnToggleTheme.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            updateTheme();
            refreshCurrentScreen();
        });

        headerPanel.add(btnToggleTheme);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Center panel for screens
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        frame.add(contentPanel, BorderLayout.CENTER);

        // Setup initial theme and show menu
        updateTheme();
        showGuestMenu();
        frame.setVisible(true);
    }

    // Apply colors to the whole application
    static void updateTheme() {
        Color bg = isDarkMode ? new Color(43, 43, 43) : new Color(245, 245, 245);
        Color fg = isDarkMode ? Color.WHITE : Color.BLACK;
        Color inputBg = isDarkMode ? new Color(60, 60, 60) : Color.WHITE;

        Color boxLight = new Color(240, 240, 240);
        Color boxDark = new Color(70, 70, 70);

        UIManager.put("Panel.background", bg);
        UIManager.put("OptionPane.background", bg);
        UIManager.put("OptionPane.messageForeground", fg);
        UIManager.put("Label.foreground", fg);
        UIManager.put("TextField.background", inputBg);
        UIManager.put("TextField.foreground", fg);
        UIManager.put("ScrollPane.background", bg);
        UIManager.put("Viewport.background", bg);

        if (isDarkMode) {
            btnToggleTheme.setBackground(boxDark);
            btnToggleTheme.setForeground(Color.WHITE);
            btnToggleTheme.setText("🌙 dark mode on");
        } else {
            btnToggleTheme.setBackground(boxLight);
            btnToggleTheme.setForeground(Color.BLACK);
            btnToggleTheme.setText("☀️ light mode on");
        }

        if (frame != null) {
            SwingUtilities.updateComponentTreeUI(frame);
        }
    }

    // Refresh screen to apply layout changes
    static void refreshCurrentScreen() {
        if (authService.loggedInUser == null) {
            showGuestMenu();
        } else if (authService.loggedInUser.isAdmin) {
            showAdminMenu();
        } else {
            showUserMenu();
        }
    }

    // Style standard buttons with red accent
    static void stylePrimaryButton(JButton btn) {
        btn.setBackground(redAccent);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Remove all components from content panel
    static void clearFrame() {
        contentPanel.removeAll();
        contentPanel.repaint();
    }

    // Show guest menu options
    static void showGuestMenu() {
        clearFrame();
        frame.setTitle("KutayTickets \"easiest way to get tickets!\"");
        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JLabel welcomeLabel = new JLabel("Welcome to KutayTickets!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        JButton btnExit = new JButton("Exit");

        stylePrimaryButton(btnLogin);
        stylePrimaryButton(btnRegister);

        // Handle login action
        btnLogin.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "Username");
            if (username == null || username.trim().isEmpty()) return;
            String password = JOptionPane.showInputDialog(frame, "Password");
            if (password == null || password.trim().isEmpty()) return;

            if (authService.login(username, password)) {
                refreshCurrentScreen();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Handle register action
        btnRegister.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "New Username");
            if (username == null || username.trim().isEmpty()) return;
            String password = JOptionPane.showInputDialog(frame, "New Password");
            if (password == null || password.trim().isEmpty()) return;

            if (authService.registerUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Registration Successful");
            } else {
                JOptionPane.showMessageDialog(frame, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Handle exit action
        btnExit.addActionListener(e -> System.exit(0));

        panel.add(welcomeLabel);
        panel.add(btnLogin);
        panel.add(btnRegister);
        panel.add(btnExit);

        contentPanel.add(panel, BorderLayout.CENTER);
        frame.revalidate();
    }

    // Show admin dashboard
    static void showAdminMenu() {
        clearFrame();
        frame.setTitle("KutayTickets - Admin Panel - " + authService.loggedInUser.username);
        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JButton btnDefine = new JButton("Define Ticket");
        JButton btnReports = new JButton("Sales Reports");
        JButton btnLogout = new JButton("Logout");

        stylePrimaryButton(btnDefine);

        // Handle ticket definition
        btnDefine.addActionListener(e -> {
            try {
                String activity = JOptionPane.showInputDialog(frame, "Activity Name");
                if (activity == null || activity.trim().isEmpty()) return;

                String time = JOptionPane.showInputDialog(frame, "Time e g 20:00");
                if (time == null || time.trim().isEmpty()) return;

                String priceStr = JOptionPane.showInputDialog(frame, "Price");
                if (priceStr == null || priceStr.trim().isEmpty()) return;
                double price = Double.parseDouble(priceStr);

                String stockStr = JOptionPane.showInputDialog(frame, "Stock Quantity");
                if (stockStr == null || stockStr.trim().isEmpty()) return;
                int stock = Integer.parseInt(stockStr);

                ticketService.defineTicket(activity, time, price, stock);
                JOptionPane.showMessageDialog(frame, "Ticket defined successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Handle sales reports display
        btnReports.addActionListener(e -> {
            List<Ticket> reports = ticketService.getSalesReports();
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

            for (Ticket t : reports) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel info = new JLabel("ID " + t.id + "   " + t.activity + "   Sold " + t.soldCount + "   Stock " + t.stock);
                row.add(info);
                listPanel.add(row);
            }

            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setPreferredSize(new Dimension(450, 350));
            JOptionPane.showMessageDialog(frame, scroll, "Sales Reports", JOptionPane.PLAIN_MESSAGE);
        });

        // Handle admin logout
        btnLogout.addActionListener(e -> {
            authService.logout();
            refreshCurrentScreen();
        });

        panel.add(new JLabel("Admin Dashboard", SwingConstants.CENTER));
        panel.add(btnDefine);
        panel.add(btnReports);
        panel.add(btnLogout);

        contentPanel.add(panel, BorderLayout.CENTER);
        frame.revalidate();
    }

    // Show customer dashboard
    static void showUserMenu() {
        clearFrame();
        frame.setTitle("KutayTickets - Customer Panel - " + authService.loggedInUser.username);
        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JButton btnSearch = new JButton("View & Buy Tickets");
        JButton btnCancel = new JButton("My Tickets / Cancel");
        JButton btnLogout = new JButton("Logout");

        stylePrimaryButton(btnSearch);

        // Show all tickets with buy buttons directly
        btnSearch.addActionListener(e -> {
            List<Ticket> results = ticketService.tickets;
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

            for (Ticket t : results) {
                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
                row.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                JLabel info = new JLabel(t.activity + "   " + t.price + " Time " + t.time + "   Stock " + t.stock);
                info.setFont(new Font("Arial", Font.BOLD, 15));

                JButton btnBuy = new JButton("Buy Ticket");
                stylePrimaryButton(btnBuy);

                // Handle buying directly from list
                btnBuy.addActionListener(buyEvent -> {
                    if (ticketService.buyTicket(t.id, authService.loggedInUser)) {
                        JOptionPane.showMessageDialog(frame, "Purchase successful");
                        Window w = SwingUtilities.getWindowAncestor(btnBuy);
                        if (w != null) w.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Purchase failed or out of stock", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                row.add(info);
                row.add(Box.createVerticalStrut(15));
                row.add(btnBuy);

                listPanel.add(row);
                listPanel.add(Box.createVerticalStrut(10));
            }

            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setPreferredSize(new Dimension(500, 400));
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            JOptionPane.showMessageDialog(frame, scroll, "Available Tickets", JOptionPane.PLAIN_MESSAGE);
        });

        // Show user tickets with cancel buttons
        btnCancel.addActionListener(e -> {
            List<Ticket> myTickets = authService.loggedInUser.purchasedTickets;

            if (myTickets.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "You have no tickets");
                return;
            }

            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

            for (Ticket t : myTickets) {
                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
                row.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                JLabel info = new JLabel(t.activity + "   " + t.price);
                info.setFont(new Font("Arial", Font.BOLD, 15));

                JButton btnCancelTicket = new JButton("Cancel Ticket");
                stylePrimaryButton(btnCancelTicket);

                // Handle cancelling directly from list
                btnCancelTicket.addActionListener(cancelEvent -> {
                    if (ticketService.cancelTicket(t.id, authService.loggedInUser)) {
                        JOptionPane.showMessageDialog(frame, "Ticket cancelled successfully");
                        Window w = SwingUtilities.getWindowAncestor(btnCancelTicket);
                        if (w != null) w.dispose();
                    }
                });

                row.add(info);
                row.add(Box.createVerticalStrut(15));
                row.add(btnCancelTicket);

                listPanel.add(row);
                listPanel.add(Box.createVerticalStrut(10));
            }

            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setPreferredSize(new Dimension(500, 400));
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            JOptionPane.showMessageDialog(frame, scroll, "My Purchased Tickets", JOptionPane.PLAIN_MESSAGE);
        });

        // Handle customer logout
        btnLogout.addActionListener(e -> {
            authService.logout();
            refreshCurrentScreen();
        });

        panel.add(new JLabel("Customer Dashboard", SwingConstants.CENTER));
        panel.add(btnSearch);
        panel.add(btnCancel);
        panel.add(btnLogout);

        contentPanel.add(panel, BorderLayout.CENTER);
        frame.revalidate();
    }
}