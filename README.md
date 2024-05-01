# RMI Chat Application

This is a simple chat application built using Java RMI (Remote Method Invocation). It allows clients to connect to a central server and communicate with each other in real-time.

## Features

- Two GUI interfaces: `ClientGui` for clients and `HealthCareGui` for healthcare providers.
- Clients can send messages to healthcare providers.
- Healthcare providers can view and remove messages sent by clients.
- Server-side logic to manage client connections and message forwarding.

## Usage

1. **Running the Server**:
   - Compile and run the `Server` class. Make sure to specify the IP address where the RMI registry will run if needed.
     ```bash
     java Server
     ```

2. **Running the Client GUIs**:
   - Compile and run the `ClientGui` class to launch the client GUI for sending messages.
     ```bash
      java ClientGui
     ```
   - Compile and run the `HealthCareGui` class to launch the healthcare provider GUI for viewing and removing messages.
     ```bash
      java HealthCareGui
     ```

## Dependencies

- Java Development Kit (JDK)
- Java RMI (included in JDK)

## Contributions

Contributions are welcome! If you'd like to contribute to this project, please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature-name`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature/your-feature-name`).
5. Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
