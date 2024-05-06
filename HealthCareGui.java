
// HealthCareGui.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HealthCareGui extends JFrame {
    private ChatServerInterface server;
    private JTextArea chatArea;
    private List<JButton> removeButtons; // List to hold remove buttons for each message

    public HealthCareGui() {
        super("HealthCareGui");

        try {
            Registry registry = LocateRegistry.getRegistry("192.168.144.254", 1098); // change this with your ip address
            // (server ip address)
            server = (ChatServerInterface) registry.lookup("ChatServer");
            server.registerHealthCareClient(new HealthCareClientImpl());

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

        removeButtons = new ArrayList<>();

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton clearButton = new JButton("Clear All Messages");
        clearButton.addActionListener(e -> clearMessages());
        bottomPanel.add(clearButton, BorderLayout.WEST);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void removeMessage(int index) {
        try {
            // Remove message from the server-side
            // You need to implement this method in ChatServer class
            server.removeMessage(index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Remove message and corresponding button from the GUI
        chatArea.setText(removeMessageAtIndex(chatArea.getText(), index));
        removeButtons.remove(index).setVisible(false);
        // Update the layout
        revalidate();
        repaint();
    }

    // Helper method to remove message at a specific index from the chat area text
    private String removeMessageAtIndex(String text, int index) {
        String[] messages = text.split("\n");
        StringBuilder newText = new StringBuilder();
        for (int i = 0; i < messages.length; i++) {
            if (i != index) {
                newText.append(messages[i]).append("\n");
            }
        }
        return newText.toString();
    }

    private void clearMessages() {
        try {
            // Clear messages on the server-side
            // You need to implement this method in ChatServer class
            server.clearMessages();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Clear messages and buttons from the GUI
        chatArea.setText("");
        for (JButton button : removeButtons) {
            button.setVisible(false);
        }
        removeButtons.clear();
    }

    private class HealthCareClientImpl extends UnicastRemoteObject implements HealthCareClientInterface {
        private static final long serialVersionUID = 1L;
        private int messageIndex = 0; // Index to keep track of messages

        protected HealthCareClientImpl() throws RemoteException {
            super();
        }

        @Override
        public void receiveMessage(String sender, String message) throws RemoteException {
            chatArea.append(sender + ": " + message + "\n");

            // Create a remove button for each new message received
            JButton removeButton = new JButton("Remove");
            removeButton.addActionListener(e -> {
                int buttonIndex = removeButtons.indexOf(removeButton);
                if (buttonIndex != -1) {
                    removeMessage(buttonIndex);
                }
            });
            removeButtons.add(removeButton);

            // Add remove button to the GUI
            JPanel messagePanel = new JPanel(new BorderLayout());
            messagePanel.add(new JLabel(sender + ": " + message), BorderLayout.CENTER);
            messagePanel.add(removeButton, BorderLayout.EAST);
            chatArea.add(messagePanel);

            // Update the layout
            revalidate();
            repaint();
            messageIndex++;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HealthCareGui::new);
    }
}
