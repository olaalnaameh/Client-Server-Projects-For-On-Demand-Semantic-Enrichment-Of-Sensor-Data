package onDemandSemantificationPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerThread extends Thread {

	Socket socket;
	BufferedReader bufferedreader;

	ServerThread(final Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		String query_file_path = null;
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Read this query_file_path as a list of files with queries
			while ((query_file_path = bufferedreader.readLine()) != null) {
				ArrayList<String> queries = readQueries(query_file_path);
				// donot create an instance of ExecuteQuery.java
				// Starts the CSPARQL engine
				ExecuteQuery eq = new ExecuteQuery(queries);
				// // Run the queries
				eq.runQueries();
			}
			socket.close();
		} catch (IOException e) {
			// line=this.getNamC:/Users/karim/Documents/PhD/SemantificationOnDemand/query/query.txte(); //reused String line for getting thread name
			System.out.println("IO Error/ Client " + this.getName() + " terminated abruptly");
		} catch (NullPointerException e) {
			// line=this.getName(); //reused String line for getting thread name
			System.out.println("Client " + this.getName() + " Closed");
		} finally {
			try {
				System.out.println("Connection Closing..");
				if (bufferedreader != null) {
					bufferedreader.close();
					System.out.println(" Socket Input Stream Closed");
				}
				if (socket != null) {
					socket.close();
					System.out.println("Socket Closed");
				}
			} catch (IOException ie) {
				System.out.println("Socket Close Error");
			}
		}// end finally
	}

	private String readQueryFile(final String pathname) throws IOException {
		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
		String lineSeparator = System.getProperty("line.separator");

		try {
			if (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine());
			}
			while (scanner.hasNextLine()) {
				fileContents.append(lineSeparator + scanner.nextLine());
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}

	private ArrayList<String> readQueries(final String pathname) throws IOException {
		ArrayList<String> queries = new ArrayList<String>();
		File folder = new File(pathname);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				System.out.println("File Name: " + file.getName());
				StringBuilder fileContents = new StringBuilder((int) file.length());
				Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
				String lineSeparator = System.getProperty("line.separator");

				try {
					if (scanner.hasNextLine()) {
						fileContents.append(scanner.nextLine());
					}
					while (scanner.hasNextLine()) {
						fileContents.append(lineSeparator + scanner.nextLine());
					}
					queries.add(fileContents.toString());
					System.out.println(fileContents.toString());
				} finally {
					scanner.close();
				}
			} else {
				System.out.println("No Query File Found....");
			}
		}
		return queries;
	}
}
