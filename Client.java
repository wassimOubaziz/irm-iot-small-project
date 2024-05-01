import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) {
        try {
            // Get the registry running on the server host and port
            Registry registry = LocateRegistry.getRegistry("localhost", 9100);

            // Look up the remote object from the registry
            Service messageService = (Service) registry.lookup("Service");

            // Send a message to the server
            messageService.sendMessage("Hello from client!");

        } catch (Exception e) {
            // Handle exception
            System.out.println("Client exception: " + e.toString());
        }
    }
}
