package blerrg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	private ServerSocket serverSocket;
	private ExecutorService executor;
	
	private BufferedReader in;
	private BufferedWriter writer;
	
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
      int portNumber = 22222;
      executor = null;
      serverSocket = new ServerSocket(portNumber);
      executor = Executors.newFixedThreadPool(5);
  }
  
  public String getClient() throws IOException {
	  String pNum = null;
	  if (BlerrgGame.clientCount < 3) {
	  		System.out.println("Waiting for clients");
	  		if (BlerrgGame.clientCount == 0) {
	  			pNum = "2";
	  			p2Socket = serverSocket.accept();
	  			Runnable worker = new RequestHandler(p2Socket);
		        executor.execute(worker);
		        p2In = new BufferedReader(new InputStreamReader(p2Socket.getInputStream()));
		        p2Out = new PrintWriter(p2Socket.getOutputStream(), true);
	  		}
	  		else if (BlerrgGame.clientCount == 1) {
	  			pNum = "3";
	  			p3Socket = serverSocket.accept();
	  			Runnable worker = new RequestHandler(p3Socket);
		        executor.execute(worker);
		        p3In = new BufferedReader(new InputStreamReader(p3Socket.getInputStream()));
		        p3Out = new PrintWriter(p3Socket.getOutputStream(), true);
	  		}
	  		else if (BlerrgGame.clientCount == 2) {
	  			pNum = "4";
	  			p4Socket = serverSocket.accept();
	  			Runnable worker = new RequestHandler(p4Socket);
		        executor.execute(worker);
		        p4In = new BufferedReader(new InputStreamReader(p4Socket.getInputStream()));
		        p4Out = new PrintWriter(p4Socket.getOutputStream(), true);
	  		}
	        BlerrgGame.clientCount += 1;
  	}
	  return pNum;
  }
  
//  public void setPlayerSend(String p) throws IOException {
//	  if (p == "2") {
//		  p2Out.write("New Player: " + p);
//	  }
//	  else if (p == "3") {
//		  
//	  }
//	  else if (p == "4") {
//		  
//	  }
//  }
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
}