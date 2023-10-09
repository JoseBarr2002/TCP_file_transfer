/***********************************************************************************************************
 ** Jose Barrera																						  **
 ** COSC 4360																							  **
 ** 10-4-2023																						      **
 ** Purpose: Make the server take in the file, hash it using the SHA-256 algorithm, and send that hash    **
 ** back to the server.																				      **
 ***********************************************************************************************************/

import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.math.BigInteger;

public class TCPServer 
{
	
	/**
	 * Entry point of the program.
	 * @param args Not used
	 */
	public static void main(String[] args) 
	{

		try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            
            while (true) {  // Infinite loop to keep server running and accepting client connections
                try (Socket connectionSocket = serverSocket.accept()) {
                    System.out.println("Connected to " + connectionSocket.getRemoteSocketAddress());

                    // Receive file
                    byte[] fileBytes = connectionSocket.getInputStream().readAllBytes();
                    System.out.println("File size in bits: " + fileBytes.length * 8 );

                    // Compute SHA-256
                    String sha256 = computeSHA256(fileBytes);
                    System.out.println("Received file SHA256 hash: " + sha256);

                    // Send SHA-256 to client
                    DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
                    out.writeUTF(sha256);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
		
		/*An object socket that is yet to be assigned to a port.*/
		//ServerSocket serverSocket;
		
		// try 
		// {
		// 		//Creates an instance of a socket
		// 	   serverSocket = new ServerSocket(9999); //creates a socket and binds it to port 9999
		// 	   //serverSocket = new ServerSocket(0); //creates a socket and binds it to next available port 
			   
		// 	   while (true)
		// 	   {
		// 		   System.out.println("TCP Server waiting for client on port " + serverSocket.getLocalPort() + "...");
				   
		// 		   //Listnes for a connection with a client
		// 		   Socket connectionSocket = serverSocket.accept();  
				   									
		// 		   System.out.println("Just connected server port # " + connectionSocket.getLocalSocketAddress() + " to client port # " + connectionSocket.getRemoteSocketAddress());
				   
		// 		   //Receive data from the client
		// 		   DataInputStream in = new DataInputStream(connectionSocket.getInputStream()); //get incoming data in bytes from connection socket
		// 		   String outText = in.readUTF();
				   
		// 		   System.out.println("RECEIVED: from IPAddress " + 
		// 					connectionSocket.getInetAddress() + " and from port " + connectionSocket.getPort() + " the data: " + outText);
		// 		   outText = outText.toUpperCase();

		// 		   //Sending data to client
		// 		   DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream()); //setup a stream for outgoing bytes of data
		// 		   out.writeUTF(outText);

		// 		   //Closes the socket
		// 		   connectionSocket.close();
				
		// 		   System.out.println();
		// 	   }
	
		// } 
		catch (IOException e)
		{
				e.printStackTrace();
		}
	}//end of main
		/**
	 * Method that encrypts the given file.
	 * @param data The file to encrypt.
	 * @return The encrypted file.
	 */
	public static String computeSHA256(byte[] data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data);
			StringBuilder hexString = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}//end of computeSHA256
}//end of class
