package blerrg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.ServerSocket;
import java.net.Socket;

public class RequestHandler implements Runnable {
  private Socket client;
  ServerSocket serverSocket = null;
  private BufferedReader in;
  private BufferedWriter writer;

    public RequestHandler(Socket client) {
      this.client = client;
    }

    @Override
    public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Thread started with name:" + Thread.currentThread().getName());
	}
    
    public void setPlayer(String n) throws IOException {
    	writer.write("setPlayer: " + n);
    	writer.newLine();
    	writer.flush();
    }

}