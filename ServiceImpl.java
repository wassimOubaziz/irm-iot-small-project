import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServiceImpl implements Service {

    private List<HealthCare> healthCares;

    protected ServiceImpl() throws RemoteException {
        super();
        healthCares = new ArrayList<>();
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

    @Override
    public void checkHeathFailed(double lat, double lon, String name) throws RemoteException {
        // send to health care Client side lat and lon and name

    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println("Received message from client: " + message);
        // Process the message as needed
        // Notify all registered clients
        for (HealthCare healthCare : healthCares) {
            healthCare.receiveMessage(message);
        }
    }

    @Override
    public void registerClient(HealthCare healthCare) throws RemoteException {
        healthCares.add(healthCare);
    }

}
