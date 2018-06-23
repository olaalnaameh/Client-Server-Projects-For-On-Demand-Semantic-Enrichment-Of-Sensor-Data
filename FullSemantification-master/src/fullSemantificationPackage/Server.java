package fullSemantificationPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static final int PORT = 4444;

	public static void main(final String[] args) throws IOException {
		new Server().runServer();
	}

	public void runServer() throws IOException {
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server up & ready for connection....");
		try {
			while (true) {
				Socket socket = serverSocket.accept();
				new ServerThread(socket).start();
			}
		} catch (IOException e) {
			// Do whatever you need to do here, like maybe deal with "socket"?
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}

			} catch (Exception e) {
				System.out.println("hmmm");
			}
		}
	}
}
