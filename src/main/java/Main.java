import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  static final Number HTTP_PORT = 4221;
  static final String HTTP_VERSION = "HTTP/1.1";

  public static void main(String[] args) {
    try {
      ServerSocket serverSocket = new ServerSocket((int) HTTP_PORT);
      
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);
      
      System.out.println(String.format("Server is running on port %d...", HTTP_PORT));

      Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
      System.out.println("Accepted new connection");

      String reqLine =  getReqLine(clientSocket);      
      String reqPath = reqLine.split(" ")[1];
      System.out.println(String.format("Requested path is: %s", reqPath));

      Boolean isValidPath = reqPath.equals("/");
      Number statusCode = isValidPath ? 200 : 404;
      String statusText = isValidPath ? "OK" : "Not Found";
      
      // Handle response
      String httpResponse = String.format("%s %d %s\r\n\r\n", HTTP_VERSION, statusCode, statusText);
      clientSocket.getOutputStream().write(httpResponse.getBytes());
      
      // Close connection
      clientSocket.close();
      serverSocket.close();
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }

  public static String getReqLine(Socket clientSocket) throws IOException {
    var reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    String reqLine = reader.readLine();
    return reqLine;
  }
}
