import java.rmi.*;

public interface HealthCareClientInterface extends Remote {
    void receiveMessage(String sender, String message) throws RemoteException;
}