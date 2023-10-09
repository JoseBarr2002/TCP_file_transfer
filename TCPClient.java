/***********************************************************************************************************
 ** Jose Barrera																						  **
 ** COSC 4360																							  **
 ** 10-4-2023																						      **
 ** Purpose: Make the client take in a file from the command line, send it to the server, hash the file   **
 ** using the SHA-256 algorith, compare the recieved server hash to its own, show the RTT, and            **
 ** calculate the throughput (in bits).																      **
 ***********************************************************************************************************/

import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.math.BigInteger;

import javax.swing.JOptionPane;

public class TCPClient 
{
	/***
	 * Method that encrypts an input String using the SHA-256 algorthim.
	 * @param input The String to encrypt.
	 * @return The encrypted String.
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

	/**
	 * Method that computes the throughput. 
	 * @param fileSizeInBits The size of the file in bits.
	 * @param timeTaken The time taken to recieve the file.
	 * @return The throughput.
	 */
	public double throughput(int fileSizeInBits, double timeTaken){

		double tp = (double) fileSizeInBits / timeTaken;

		return tp;
	}

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
