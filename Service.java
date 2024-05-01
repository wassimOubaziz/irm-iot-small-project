import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Service extends Remote {
    public String getWeatherForecast(double lat, double lon) throws RemoteException;

    public void checkHeathFailed(double lat, double lon, String name) throws RemoteException;

    void sendMessage(String message) throws RemoteException;

    void registerClient(HealthCare healthCare) throws RemoteException;
}
