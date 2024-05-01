
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        try {
            // Set hostname for the server using javaProperty
            System.setProperty("java.rmi.server.hostname", "localhost");
            System.out.println("Server is running on port 9100...");

            // Create an instance of your service implementation
            ServiceImpl serviceImpl = new ServiceImpl();

            // Export the service object
            Service service = (Service) UnicastRemoteObject.exportObject(serviceImpl, 0);

            // Get the registry running on the specified host and port
            Registry registry = LocateRegistry.getRegistry("localhost", 9100);

            // Bind services to the registry
            registry.rebind("Service", service);

        } catch (Exception e) {
            // Handle exception
            System.out.println("Server exception: " + e);
        }
    }

}
