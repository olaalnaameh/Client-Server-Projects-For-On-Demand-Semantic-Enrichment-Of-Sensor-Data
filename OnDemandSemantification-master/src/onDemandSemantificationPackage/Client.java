package onDemandSemantificationPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(final String[] args) throws UnknownHostException, IOException {
		// String user = args[0];
		Socket socket = new Socket("localhost", 7777);
		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader bufferedReader = new java.io.BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter Path of the Queries...");
		try {
			while (true) {
				String query_file_path = bufferedReader.readLine();
				// printWriter.println(query_file_path + ":" + readerInput);
				printWriter.println(query_file_path);
			}
		} finally {
			socket.close();
			printWriter.close();
			// bufferedReader.close();
			System.out.println("Connection Closed");
		}
	}
}
