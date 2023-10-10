
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
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

				Socket connectionSocket = serverSocket.accept();
				System.out.println("Connected to " + connectionSocket.getRemoteSocketAddress());

				// System.out.println("recieving file");

				/****/
				// Receive file size first
				DataInputStream in = new DataInputStream(connectionSocket.getInputStream());
				int fileSize = in.readInt(); // Read the size of the file first

				// Receive file data in chunks
				byte[] buffer = new byte[4096]; // 4KB buffer
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

				int bytesRead;
				int totalBytesRead = 0;
				while (totalBytesRead < fileSize && (bytesRead = in.read(buffer)) != -1) {
					byteArrayOutputStream.write(buffer, 0, bytesRead);
					totalBytesRead += bytesRead;
				}
				byte[] fileBytes = byteArrayOutputStream.toByteArray();
				System.out.println("Received file size in bits: " + fileBytes.length * 8);
				/****/

				System.out.println("Received file SHA256 hash: " + computeSHA256(fileBytes));

				// Sending the data to the client
				DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
				out.writeUTF(computeSHA256(fileBytes));
				out.flush();
				// System.out.println("Hash sent to client.");

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
	public static String computeSHA256(byte[] data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data);
			StringBuilder hexString = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}// end of computeSHA256
}// end of class
