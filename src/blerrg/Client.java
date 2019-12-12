package blerrg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	private Socket echoSocket;
	private PrintWriter out;
	private BufferedReader in;

  public Client() throws IOException {
      String hostName = BlerrgGame.hostIP;
      int portNumber = 42069;
      echoSocket = new Socket(hostName, portNumber);
      out = new PrintWriter(echoSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
  }
  
  public void updateServer(String msg) {
	  out.println(msg);
  }
  
  public String getUpdates() throws IOException {
	  
	  //check input
	  
	  String input;
	  
	  try {input = in.readLine();
	  } catch (IOException e) {input = "!:close|";
	  e.printStackTrace();}
	  if (input.equals("!:close|")) { echoSocket.close(); }
	  
	  //BlerrgGame.debugPrint("Client recieved update: ", input);
	  return input;
  }
}