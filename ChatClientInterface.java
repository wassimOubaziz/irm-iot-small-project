
// ChatClientInterface.java
import java.rmi.*;

public interface ChatClientInterface extends Remote {
    void receiveMessage(String sender, String message) throws RemoteException;
}
