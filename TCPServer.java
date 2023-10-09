/*Jose Barrera
 * COSC 4360
 * 10-4-2023
 * Purpose: Right Now, I don't know.
 */

import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.math.BigInteger;

public class TCPServer 
{
	/**
	 * Method that encrypts a String using the SHA-256 algorthim.
	 * @param input The String to encrypt.
	 * @return An encrypted String.
	 * @throws NoSuchAlgorithmException 
	 */
	public String encryptString(String input) throws NoSuchAlgorithmException
 	{
        //MesageDigest works with MD2, MD5, SHA-1, SHA-224, SHA-256
        //SHA-384, and SHA-512
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] messageDigest = md.digest(input.getBytes());

        BigInteger bigInt = new BigInteger(1, messageDigest);
        
        return bigInt.toString(16);
    }
	
	public static void main(String[] args) 
	{
				
		ServerSocket serverSocket;
		
		try 
		{
		
			   serverSocket = new ServerSocket(9999); //creates a socket and binds it to port 9999
			   //serverSocket = new ServerSocket(0); //creates a socket and binds it to next available port 
			   
			   while (true)
			   {
			   
				   System.out.println("TCP Server waiting for client on port " + serverSocket.getLocalPort() + "...");
				   
				   Socket connectionSocket = serverSocket.accept();  //listens for connection and 
				   										// creates a connection socket for communication
				   
				   System.out.println("Just connected server port # " + connectionSocket.getLocalSocketAddress() + " to client port # " + connectionSocket.getRemoteSocketAddress());
				   
				   DataInputStream in = new DataInputStream(connectionSocket.getInputStream()); //get incoming data in bytes from connection socket
				   
				   String outText = in.readUTF();
				   
				   System.out.println("RECEIVED: from IPAddress " + 
							connectionSocket.getInetAddress() + " and from port " + connectionSocket.getPort() + " the data: " + outText);
				   
				   outText = outText.toUpperCase();
				   
				   DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream()); //setup a stream for outgoing bytes of data
				   
				   out.writeUTF(outText);
				   
				   connectionSocket.close();  //close connection socket after this exchange
				   
				   System.out.println();
			   }
	
		} 
		catch (IOException e)
		{
				e.printStackTrace();
		}
	}

}
