
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

/**
 * Class that acts as a server.
 */
public class TCPServer {

	/**
	 * Entry point of the program.
	 * 
	 * @param args Not used
	 */
	public static void main(String[] args) {

		try (ServerSocket serverSocket = new ServerSocket(9999)) {
			while (true) {
				System.out.println();
				System.out.println("To exit server, you will have to manually 'kill' the program. >:)");
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

				Socket connectionSocket = serverSocket.accept();
				System.out.println("Connected to " + connectionSocket.getRemoteSocketAddress());

				// System.out.println("recieving file");

				/****/
				// Receive file size
				DataInputStream in = new DataInputStream(connectionSocket.getInputStream());
				int fileSize = in.readInt();

				// Receive file in chunks (packets)
				byte[] packets = new byte[4096]; // 4000 byte storage
				ByteArrayOutputStream bAOS = new ByteArrayOutputStream();

				// Reading the file
				int bytesRead;
				int totalBytesRead = 0;
				while (totalBytesRead < fileSize && (bytesRead = in.read(packets)) != -1) {
					bAOS.write(packets, 0, bytesRead);
					totalBytesRead += bytesRead;
				}
				byte[] fileBytes = bAOS.toByteArray();
				System.out.println("Received file size in bits: " + fileBytes.length * 8);
				/****/

				System.out.println("Received file SHA256 hash: " + encrypt(fileBytes));

				// Sending the data to the client
				DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
				out.writeUTF(encrypt(fileBytes));
				out.flush(); // this sh*t too
				// System.out.println("Hash sent to client.");

				// Closing streams
				in.close();
				out.close();
				// System.out.println("End of connection");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}// end of main

	/**
	 * Method that encrypts the given file.
	 * 
	 * @param data The file to encrypt.
	 * @return The encrypted file.
	 */
	public static String encrypt(byte[] data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data);
			StringBuilder hexSB = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexSB.append('0');
				hexSB.append(hex);
			}
			return hexSB.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}// end of encrypt
}// end of class
