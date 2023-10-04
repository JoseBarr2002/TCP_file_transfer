import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

public class TCPClient 
{

	public static void main(String[] args) 
	{
		String serverName = "localhost";
		//String serverName = "192.168.1.135";
		int port = 9999;
		
		try 
		{
			System.out.println("Connecting to " + serverName + " on port " + port);
			
			Socket clientSocket = new Socket(serverName, port);  //create socket for connecting to server
			
			System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
			
			OutputStream outToServer = clientSocket.getOutputStream();  //stream of bytes
			
			DataOutputStream out = new DataOutputStream(outToServer);
			
			String outText = JOptionPane.showInputDialog("Enter Client Message: ");
			
			System.out.println("TCP Client says: " + outText);
			
			out.writeUTF(outText);
			
			InputStream inFromServer = clientSocket.getInputStream();  //stream of bytes
			
			DataInputStream in = new DataInputStream(inFromServer);
			
			System.out.println("TCP Server says: " + in.readUTF());
			
			clientSocket.close();
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
