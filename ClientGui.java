
// ClientGui.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ClientGui extends JFrame {
    private ChatServerInterface server;
    private JTextArea chatArea;
    private JTextField messageField;

    public ClientGui(String name) {
        super(name);

        try {
            Registry registry = LocateRegistry.getRegistry("192.168.43.59", 1098); // change this with your ip address
                                                                                   // (server ip address)
            server = (ChatServerInterface) registry.lookup("ChatServer");
            server.registerClient(name, new ChatClientImpl());

            String ipAddress = getPublicIPAddress();
            System.out.println("Public IP Address: " + ipAddress);
            Map<String, Double> coordinates = getCoordinatesFromIPAddress(ipAddress);
            System.out.println("Latitude: " + coordinates.get("latitude"));
            System.out.println("Longitude: " + coordinates.get("longitude"));
            String a = server.getWeatherForecast(36.35, 6.6);
            System.err.println(a);
            setupUI();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private void setupUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.addActionListener(e -> sendMessage());
        bottomPanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                server.sendMessageToHealthCare(getTitle(), message);
                chatArea.append("You: " + message + "\n");
                messageField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class ChatClientImpl extends UnicastRemoteObject implements ChatClientInterface {
        private static final long serialVersionUID = 1L;

        protected ChatClientImpl() throws RemoteException {
            super();
        }

        @Override
        public void receiveMessage(String sender, String message) throws RemoteException {
            // Not used in client GUI
        }
    }

    public Map<String, Double> getCoordinatesFromIPAddress(String ipAddress) {
        Map<String, Double> coordinates = new HashMap<>();
        try {
            // URL of the ip-api.com API
            URL url = new URL("http://ip-api.com/json/" + ipAddress);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            String jsonResponse = response.toString();
            String[] parts = jsonResponse.split(",");
            for (String part : parts) {
                if (part.contains("\"lat\":")) {
                    String[] latPart = part.split(":");
                    double latitude = Double.parseDouble(latPart[1].trim());
                    coordinates.put("latitude", latitude);
                }
                if (part.contains("\"lon\":")) {
                    String[] lonPart = part.split(":");
                    double longitude = Double.parseDouble(lonPart[1].trim());
                    coordinates.put("longitude", longitude);
                }
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    public String getPublicIPAddress() {
        try {
            // URL of the service that provides the public IP address
            URL url = new URL("https://api.ipify.org/?format=text");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String ipAddress = reader.readLine();

            // Close the connection
            connection.disconnect();

            return ipAddress;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog("Enter your name:");
        if (name != null && !name.isEmpty()) {
            SwingUtilities.invokeLater(() -> new ClientGui(name));
        }
    }
}
