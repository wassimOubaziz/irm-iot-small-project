import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        try {
            // Set hostname for the server using javaProperty
            System.setProperty("java.rmi.server.hostname", "192.168.40.20");
            System.out.println("Server is running on port 9100...");

            // Create instances of your services
            ServiceImpl weather = new ServiceImpl();

            Service service = (Service) UnicastRemoteObject.exportObject(weather, 0);

            // Create registry
            Registry registry = LocateRegistry.createRegistry(9100);

            // Bind services to the registry
            registry.rebind("Service", service);

        } catch (Exception e) {
            // Handle exception
            System.out.println("Server exception: " + e.toString());
        }
    }
}
