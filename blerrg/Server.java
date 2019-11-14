package blerrg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private ServerSocket serverSocket;
	
	private Socket p2Socket;
	private BufferedReader p2In;
	private PrintWriter p2Out;
	
	private Socket p3Socket;
	private BufferedReader p3In;
	private PrintWriter p3Out;
	
	private Socket p4Socket;
	private BufferedReader p4In;
	private PrintWriter p4Out;
	
  public Server() throws IOException {
	  System.out.println("Start of main");
      int portNumber = 42069;
      serverSocket = new ServerSocket(portNumber);
  }
  
  public String getClient(int cCount) throws IOException {
	  String pNum = null;
	  if (cCount < 3) {
	  		System.out.println("Waiting for clients");
	  		if (cCount == 0) {
	  			pNum = "2";
	  			p2Socket = serverSocket.accept();
		        p2In = new BufferedReader(new InputStreamReader(p2Socket.getInputStream()));
		        p2Out = new PrintWriter(p2Socket.getOutputStream(), true);
	  		}
	  		else if (cCount == 1) {
	  			pNum = "3";
	  			p3Socket = serverSocket.accept();
		        p3In = new BufferedReader(new InputStreamReader(p3Socket.getInputStream()));
		        p3Out = new PrintWriter(p3Socket.getOutputStream(), true);
	  		}
	  		else if (cCount == 2) {
	  			pNum = "4";
	  			p4Socket = serverSocket.accept();
		        p4In = new BufferedReader(new InputStreamReader(p4Socket.getInputStream()));
		        p4Out = new PrintWriter(p4Socket.getOutputStream(), true);
	  		}
  	}
	  return pNum;
  }
  
  
  public void sendToClient(String msg, String p) {
	  if (p == "2") {
		  p2Out.println(msg);
	  }
	  else if (p == "3") {
		  p3Out.println(msg);
	  }
	  else if (p == "4") {
		  p4Out.println(msg);
	  }
  }
  
  public String get2Updates() throws IOException {
	  String input2 = p2In.readLine();
	  return input2;
  }
  public String get3Updates() throws IOException {
	  String input3 = p3In.readLine();
	  return input3;
  }
  public String get4Updates() throws IOException {
	  String input4 = p4In.readLine();
	  return input4;
  }
}