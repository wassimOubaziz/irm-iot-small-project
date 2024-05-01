import javax.swing.JOptionPane;

public class HealthCare {

    // Other GUI related methods and fields

    // Method to receive and display messages from the server
    public void receiveMessage(String message) {
        JOptionPane.showMessageDialog(null, "Message from server: " + message);
        // Display the message in the GUI
    }
}