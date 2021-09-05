import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket serverSocket = new ServerSocket(5109);
			System.out.println("System is booted up and waiting for clients to conect");
			
			while(true) {
				Socket clientSocket= serverSocket.accept();
			    System.out.println(" A New client is  now connected to the server ");
		
			    session clientSession = new session(clientSocket);
			    clientSession.start();
			} 
	}catch (IOException e) {
			e.printStackTrace();
		}
		

}
}
