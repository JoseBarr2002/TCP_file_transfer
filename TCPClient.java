/***********************************************************************************************************
 ** Jose Barrera																						  **
 ** COSC 4360																							  **
 ** 10-4-2023																						      **
 ** Purpose: Make the client take in a file from the command line, send it to the server, hash the file   **
 ** using the SHA-256 algorith, compare the recieved server hash to its own, show the RTT, and            **
 ** calculate the throughput (in bits).																      **
 ***********************************************************************************************************/

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.math.BigInteger;

import javax.swing.JOptionPane;

public class TCPClient 
{
	
	/**
	 * Entry point of the program.
	 * @param args Used to take in the user's file directory.
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		/**The file path taken from the command line.*/
		String filePath = args[0];
		/**The server's name.*/
		String serverName = "localhost";
		//String serverName = "192.168.1.135";
		/**The server's port.*/
		int port = 9999;

		//Read the file in bytes
		byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
		//hash the file
		String sha256Client = computeSHA256(fileBytes);

		//Attemp to connect to server
		System.out.println("Connecting to " + serverName + " on port " + port);

		try (Socket clientSocket = new Socket("localhost", 9999)) {
			//Estabished connection
			System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());

			//sends file in bytes to the server and starts the timer
			clientSocket.getOutputStream().write(fileBytes);
			long startTime = System.currentTimeMillis();

			//receives data from the server and stops the timer
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			String sha256Server = in.readUTF();
			long endTime = System.currentTimeMillis();

			//RTT in seconds
			double timeTaken = (endTime - startTime) / 1000.0;
			// throughput in Mbps
			double throughput = (fileBytes.length * 8) / (1000000.0 * timeTaken);

			//conditions
			if (sha256Client.equals(sha256Server)) {
				System.out.println("Successfully sent!");
				System.out.println("File name: " + filePath);
				System.out.println("SHA-256 hash: " + sha256Client);
				System.out.println("File size in bits: " + fileBytes.length * 8 );
				System.out.println("Time taken: " + timeTaken + " s");
				System.out.println("Throughput: " + String.format("%.2f", throughput) + " Mbps");
			} 
			else {
				System.out.println("Error!");
				System.out.println("File name: " + filePath);
				System.out.println("SHA-256 hash: " + sha256Client);
				System.out.println("File size in bits: " + fileBytes.length * 8 );
			}





		}
		// try 
		// {
		// 	System.out.println("Connecting to " + serverName + " on port " + port);
			
		// 	//Socket to connect to the server.
		// 	Socket clientSocket = new Socket(serverName, port);
			
		// 	System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());

		//     //Sends data to the server
		// 	OutputStream outToServer = clientSocket.getOutputStream();  //stream of bytes
		// 	DataOutputStream out = new DataOutputStream(outToServer);
		// 	String outText = JOptionPane.showInputDialog("Enter Client Message: ");
		// 	System.out.println("TCP Client says: " + outText);
		// 	out.writeUTF(outText);
			
		// 	//Receives data from the server
		// 	InputStream inFromServer = clientSocket.getInputStream();  //stream of bytes
		// 	DataInputStream in = new DataInputStream(inFromServer);
		// 	System.out.println("TCP Server says: " + in.readUTF());
			
		// 	//The socket is closed
		// 	clientSocket.close();
			
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
