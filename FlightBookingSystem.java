import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Random;


class Flight implements Serializable {
    String flightNumber;
    String origin;
    String destination;
    String departureTime;

    public Flight(String flightNumber, String origin, String destination, String departureTime) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "Flight " + flightNumber + " | " + departureTime;
    }
}


class Customer implements Serializable {
    private String name;
    private int age;
    private String email;
    private Flight flight;

    public Customer(String name, int age, String email, Flight flight) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.flight = flight;
    }

    // Getters for customer details
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public Flight getFlight() {
        return flight;
    }

    @Override
    public String toString() {
        return "Customer: " + name + " | Age: " + age + " | Email: " + email + " | Flight: " + flight;
    }
}

// Main GUI class for flight booking system
public class FlightBookingSystem extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel mainPanel;
    private JTextField originField;
    private JTextField destinationField;
    private JTextArea resultArea;
    
    private List<Flight> flightList;
    private LinkedList<User> userList;
    private PriorityQueue<Ticket> ticketQueue;
    private List<Ticket> bookedTickets;
    private User currentUser;

    public FlightBookingSystem() {
        initializeData();
        showLoginScreen();
    }

    private void initializeData() {
        flightList = new ArrayList<>();
        userList = new LinkedList<>();
        ticketQueue = new PriorityQueue<>();
        bookedTickets = new ArrayList<>();

        populateFlights();
        populateUsers();
    }

    private void populateUsers() {
        userList.add(new User("admin", "admin123", true));
        userList.add(new User("user", "user123", false));
    }

    private void populateFlights() {
        flightList.add(new Flight("IA101", "Delhi", "Mumbai", "8:00 AM"));
flightList.add(new Flight("IA102", "Mumbai", "Bangalore", "9:30 AM"));
flightList.add(new Flight("IA103", "Chennai", "Hyderabad", "11:00 AM"));
flightList.add(new Flight("IA104", "Delhi", "Kolkata", "1:00 PM"));
flightList.add(new Flight("IA105", "Pune", "Delhi", "2:45 PM"));
flightList.add(new Flight("IA106", "Kolkata", "Mumbai", "4:15 PM"));
flightList.add(new Flight("IA107", "Bangalore", "Chennai", "6:30 PM"));
flightList.add(new Flight("IA108", "Hyderabad", "Delhi", "7:45 PM"));
flightList.add(new Flight("IA109", "Ahmedabad", "Pune", "9:00 PM"));
flightList.add(new Flight("IA110", "Goa", "Bangalore", "10:30 PM"));
    }

    private void showLoginScreen() {
        getContentPane().removeAll();
        setTitle("Flight Booking System - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel(""));
        loginPanel.add(loginButton);

        loginButton.addActionListener(e -> validateLogin());
        add(loginPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void validateLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Optional<User> user = userList.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (user.isPresent()) {
            currentUser = user.get();
            showMainScreen();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMainScreen() {
        getContentPane().removeAll();
        setTitle("Flight Booking System - Welcome " + currentUser.getUsername());
        setSize(800, 600);

        mainPanel = new JPanel(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem searchItem = new JMenuItem("Search Flights");
        JMenuItem viewTicketsItem = new JMenuItem("View Tickets");
        if (currentUser.isAdmin()) {
            JMenuItem managementItem = new JMenuItem("Management");
            menu.add(managementItem);
            managementItem.addActionListener(e -> showManagementScreen());
        }
        
        menu.add(searchItem);
        menu.add(viewTicketsItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        searchItem.addActionListener(e -> showSearchScreen());
        viewTicketsItem.addActionListener(e -> showTicketsScreen());

        showSearchScreen();

        revalidate();
        repaint();
    }

    private void showSearchScreen() {
        mainPanel.removeAll();

        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Flights"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Origin:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        originField = new JTextField(20);
        searchPanel.add(originField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        searchPanel.add(new JLabel("Destination:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        destinationField = new JTextField(20);
        searchPanel.add(destinationField, gbc);

        JButton searchButton = new JButton("Search Flights");
        searchButton.setBackground(new Color(66, 134, 244));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        searchPanel.add(searchButton, gbc);

        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setMargin(new Insets(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Flights"));

        JButton bookButton = new JButton("Book Selected Flight");
        bookButton.setBackground(new Color(76, 175, 80));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);

        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContent.add(searchPanel, BorderLayout.NORTH);
        mainContent.add(scrollPane, BorderLayout.CENTER);
        mainContent.add(bookButton, BorderLayout.SOUTH);

        mainPanel.add(mainContent);

        searchButton.addActionListener(e -> searchFlights());
        bookButton.addActionListener(e -> showBookingDialog());

        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchFlights();
                }
            }
        };
        originField.addKeyListener(enterListener);
        destinationField.addKeyListener(enterListener);

        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

    private void showTicketsScreen() {
        mainPanel.removeAll();

        JTextArea ticketArea = new JTextArea();
        ticketArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(ticketArea);

        StringBuilder sb = new StringBuilder("Booked Tickets:\n\n");
        PriorityQueue<Ticket> tempQueue = new PriorityQueue<>(ticketQueue);
        while (!tempQueue.isEmpty()) {
            Ticket ticket = tempQueue.poll();
            if (currentUser.isAdmin() || 
                ticket.getCustomer().getEmail().equals(currentUser.getUsername())) {
                sb.append(ticket.toString()).append("\n");
            }
        }
        ticketArea.setText(sb.toString());

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showManagementScreen() {
        if (!currentUser.isAdmin()) return;

        JDialog dialog = new JDialog(this, "Management", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JPanel statsPanel = new JPanel(new GridLayout(3, 2));
        statsPanel.add(new JLabel("Total Flights:"));
        statsPanel.add(new JLabel(String.valueOf(flightList.size())));
        statsPanel.add(new JLabel("Total Tickets:"));
        statsPanel.add(new JLabel(String.valueOf(ticketQueue.size())));
        statsPanel.add(new JLabel("Total Users:"));
        statsPanel.add(new JLabel(String.valueOf(userList.size())));

        dialog.add(statsPanel, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void searchFlights() {
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();

        if (origin.isEmpty() || destination.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both origin and destination cities.", 
                "Input Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder results = new StringBuilder();
        results.append("Available Flights:\n\n");
        boolean found = false;

        for (Flight flight : flightList) {
            if (flight.origin.equalsIgnoreCase(origin) && 
                flight.destination.equalsIgnoreCase(destination)) {
                results.append("➜ ").append(flight.toString()).append("\n");
                found = true;
            }
        }

        if (!found) {
            results.append("No flights found for the specified route.\n");
            results.append("Try searching for different cities or dates.");
        }

        resultArea.setText(results.toString());
        resultArea.setCaretPosition(0); 
    }

    private void showBookingDialog() {
        String selectedText = resultArea.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a flight to book.");
            return;
        }

        Flight selectedFlight = flightList.stream()
                .filter(f -> selectedText.contains(f.flightNumber))
                .findFirst()
                .orElse(null);

        if (selectedFlight == null) return;

        JDialog bookingDialog = new JDialog(this, "Book Ticket", true);
        bookingDialog.setSize(400, 300);
        bookingDialog.setLayout(new GridLayout(6, 2, 5, 5));

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField emailField = new JTextField();
        JComboBox<Ticket.TicketClass> classCombo = new JComboBox<>(Ticket.TicketClass.values());

        bookingDialog.add(new JLabel("Name:"));
        bookingDialog.add(nameField);
        bookingDialog.add(new JLabel("Age:"));
        bookingDialog.add(ageField);
        bookingDialog.add(new JLabel("Email:"));
        bookingDialog.add(emailField);
        bookingDialog.add(new JLabel("Class:"));
        bookingDialog.add(classCombo);

        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.addActionListener(e -> {
            try {
                Customer customer = new Customer(
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    emailField.getText(),
                    selectedFlight
                );

                Ticket ticket = new Ticket(
                    customer,
                    selectedFlight,
                    (Ticket.TicketClass) classCombo.getSelectedItem(),
                    calculatePrice((Ticket.TicketClass) classCombo.getSelectedItem())
                );

                ticketQueue.offer(ticket);
                bookedTickets.add(ticket);
                saveTicketToFile(ticket);

                JOptionPane.showMessageDialog(bookingDialog, 
                    "Ticket booked successfully!\n" + ticket.toString());
                bookingDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(bookingDialog, 
                    "Please enter valid numeric values.");
            }
        });

        bookingDialog.add(confirmButton);
        bookingDialog.setLocationRelativeTo(this);
        bookingDialog.setVisible(true);
    }

    private double calculatePrice(Ticket.TicketClass ticketClass) {
        switch (ticketClass) {
            case ECONOMY: return 200.0;
            case BUSINESS: return 500.0;
            case FIRST: return 1000.0;
            default: return 200.0;
        }
    }

    private void saveTicketToFile(Ticket ticket) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("tickets.ser", true))) {
            out.writeObject(ticket);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving ticket: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FlightBookingSystem().setVisible(true);
        });
    }
}
