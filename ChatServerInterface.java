
// ChatServerInterface.java
import java.rmi.*;
import java.util.List;

public interface ChatServerInterface extends Remote {
    void registerClient(String name, ChatClientInterface client) throws RemoteException;

    void registerHealthCareClient(HealthCareClientInterface client) throws RemoteException;

    void sendMessageToHealthCare(String sender, String message) throws RemoteException;

    // Method to remove message at a specific index
    void removeMessage(int index) throws RemoteException;

    // Method to clear all messages
    void clearMessages() throws RemoteException;

    public String getWeatherForecast(double lat, double lon) throws RemoteException;

}
