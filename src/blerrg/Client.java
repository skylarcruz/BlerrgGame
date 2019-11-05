package blerrg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private String userInput;
	
	private Socket echoSocket;
	private PrintWriter out;
	private BufferedReader in;
	//private BufferedReader stdIn;

  public Client() throws IOException {
      String hostName = "127.0.0.1";
      int portNumber = 22222;
      echoSocket = new Socket(hostName, portNumber);
      out = new PrintWriter(echoSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
      //stdIn = new BufferedReader(new InputStreamReader(System.in));
  }
  
  public void updateServer() {
	  // Send stuff to Server
  }
  
  public String getUpdates() throws IOException {
	  String input = in.readLine();
	  return input;
  }
}