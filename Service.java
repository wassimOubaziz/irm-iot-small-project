import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Service extends Remote {
    public String getWeatherForecast(double lat, double lon) throws RemoteException;
}
