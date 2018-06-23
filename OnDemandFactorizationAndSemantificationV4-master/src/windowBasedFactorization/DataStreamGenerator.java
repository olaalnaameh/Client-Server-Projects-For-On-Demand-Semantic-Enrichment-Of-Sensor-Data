package windowBasedFactorization;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.larkc.csparql.cep.api.JSONData;
import eu.larkc.csparql.cep.api.JSONStream;

public class DataStreamGenerator extends JSONStream implements Runnable {

	// Add the factorization code here in this class
	protected final Logger logger = LoggerFactory
			.getLogger(DataStreamGenerator.class);
	private boolean keepRunning = false;
	private static long systemTime;
	private static JSONData q;
	private static WebsocketClientReceivingNRData clientEndPoint = null;

	public DataStreamGenerator(final String iri) {
		super(iri);
	}

	public void pleaseStop() {
		keepRunning = false;
	}

	@Override
	public void run() {
		// Initialize and connect Client here with the source at node red
		keepRunning = true;
		try {
			clientEndPoint = new WebsocketClientReceivingNRData(new URI("ws://127.0.0.1:1880/test2"));
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (keepRunning) {
			// Add message handlet of the client here
			clientEndPoint.addMessageHandler(new WebsocketClientReceivingNRData.MessageHandler() {

				@Override
				public void handleMessage(final String message) {
					if (message.length() > 2) {
						// System.out.println("DataStreamGenerator.class- The Message is :" + message);
						// System.out.println("Message Arrival Time: " + System.currentTimeMillis());
						q = new JSONData(message, System.currentTimeMillis());
						put(q);

					}
				}
			});
		}
	}

}
