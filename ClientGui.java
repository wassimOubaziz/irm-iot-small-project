
// ClientGui.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

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

    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog("Enter your name:");
        if (name != null && !name.isEmpty()) {
            SwingUtilities.invokeLater(() -> new ClientGui(name));
        }
    }
}
