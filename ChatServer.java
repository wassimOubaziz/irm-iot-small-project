import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer implements ChatServerInterface {
    private static final long serialVersionUID = 1L;
    private Map<String, ChatClientInterface> connectedClients;
    private HealthCareClientInterface healthCareClient;
    private List<String> messages; // List to hold messages

    public ChatServer() {
        connectedClients = new HashMap<>();
        messages = new ArrayList<>();
    }

    public static void main(String[] args) {
        try {
            // Set hostname for the server using javaProperty
            System.setProperty("java.rmi.server.hostname", "192.168.43.59");
            System.out.println("Server is running on port 9100...");

            ChatServer server = new ChatServer();

            ChatServerInterface stub = (ChatServerInterface) UnicastRemoteObject.exportObject(server, 0);

            Registry registry = LocateRegistry.createRegistry(9100);
            registry.rebind("ChatServer", stub);

            System.out.println("Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void registerClient(String name, ChatClientInterface client) throws RemoteException {
        connectedClients.put(name, client);
        System.out.println(name + " connected.");
    }

    @Override
    public synchronized void registerHealthCareClient(HealthCareClientInterface client) throws RemoteException {
        healthCareClient = client;
        System.out.println("HealthCare client registered.");
    }

    @Override
    public synchronized void sendMessageToHealthCare(String sender, String message) throws RemoteException {
        if (healthCareClient != null) {
            // Add message to the list
            messages.add(sender + ": " + message);
            // Notify the healthcare client of the new message
            healthCareClient.receiveMessage(sender, message);
        } else {
            System.out.println("HealthCare client not registered.");
        }
    }

    @Override
    public synchronized void removeMessage(int index) throws RemoteException {
        if (index >= 0 && index < messages.size()) {
            messages.remove(index);
        }
    }

    @Override
    public synchronized void clearMessages() throws RemoteException {
        messages.clear();
    }

    @Override
    public String getWeatherForecast(double lat, double lon) throws RemoteException {
        try {
            // Construct the URL for the API request
            String apiUrl = "https://api.open-meteo.com/v1/forecast?";
            String queryString = String.format(
                    "latitude=%.2f&longitude=%.2f&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,daylight_duration,sunshine_duration,precipitation_sum,rain_sum,showers_sum,snowfall_sum,wind_speed_10m_max",
                    lat, lon);

            String urlString = apiUrl + queryString;

            // Create a URL object
            URL url = new URL(urlString);

            // Create an HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Close the connection
            connection.disconnect();

            // Return the response as a string
            return response.toString();
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
            return "Error fetching weather data";
        }
    }
}
