package fullSemantificationPackage;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.larkc.csparql.cep.api.RdfQuadruple;
import eu.larkc.csparql.cep.api.RdfStream;

public class SemantifiedDataStreamGenerator extends RdfStream implements Runnable {

	protected final Logger logger = LoggerFactory
			.getLogger(SemantifiedDataStreamGenerator.class);
	private boolean keepRunning = false;
	private static long systemTime;
	private static RdfQuadruple q;
	// private static int totalObservations = 0;
	private static WebsocketClientReceivingNodeRedData clientEndPoint = null;

	public SemantifiedDataStreamGenerator(final String iri) {
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
			clientEndPoint = new WebsocketClientReceivingNodeRedData(new URI("ws://127.0.0.1:1880/fullSemantification"));
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (keepRunning) {
			// Add message handlet of the client here
			clientEndPoint.addMessageHandler(new WebsocketClientReceivingNodeRedData.MessageHandler() {

				@Override
				public void handleMessage(final String message) {
					// System.out.println("The Message is :" + message);
					try {
						JSONArray array = new JSONArray(message);
						// totalObservations = totalObservations + array.length();
						// System.out.println("Total number of Observations/Measurements: " + totalObservations);
						if (array != null) {
							semantify(array);
						}// End of if(array!=null)
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// kgraph.write(System.out, "N3");
				}
			});

		}
	}// End of Run()

	//////////////////////// Semantify//////////////////////////////////////////////
	public void semantify(final JSONArray jsonArray) throws JSONException {
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			Iterator<?> keys = json.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				switch (key) {
				case "type":
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("observation"),
							"http://www.w3.org/1999/02/22-rdf-syntax-ns#" + key,
							"http://knoesis.wright.edu/ssw/ont/weather.owl#" + json.getString(key), systemTime);
					put(q);
					break;

				case "procedure":
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("observation"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#procedure",
							"http://knoesis.wright.edu/ssw/" + json.getString(key), systemTime);
					put(q);
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString(key),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#generatedObservation",
							"http://knoesis.wright.edu/ssw/" + json.getString("observation"), systemTime);
					put(q);
					break;
				case "observedProperty":
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("observation"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#" + key,
							"http://knoesis.wright.edu/ssw/ont/weather.owl#" + json.getString(key), systemTime);
					put(q);
					break;

				case "result":
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString(key),
							"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#MeasureData", systemTime);
					put(q);
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("observation"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#" + key,
							"http://knoesis.wright.edu/ssw/" + json.getString(key), systemTime);
					put(q);

					break;

				case "floatValue":
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("result"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#" + key,
							BigDecimal.valueOf(json.getDouble(key)).toString() + "^^http://www.w3.org/2001/XMLSchema#float", systemTime);
					put(q);
					break;
				case "uom":
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("result"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#" + key,
							"http://knoesis.wright.edu/ssw/ont/weather.owl#" + json.getString(key), systemTime);
					put(q);
					break;

				case "samplingTime":
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("observation"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#" + key,
							"http://knoesis.wright.edu/ssw/" + json.getString(key), systemTime);
					put(q);

					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString(key), "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
							"http://www.w3.org/2006/time#Instant", systemTime);
					put(q);
					break;

				case "inXSDDateTime":
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("samplingTime"), "http://www.w3.org/2006/time#" + key,
							BigDecimal.valueOf(json.getDouble(key)).toString() + "^^http://www.w3.org/2001/XMLSchema#dateTime", systemTime);
					put(q);
					break;

				case "ID":
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("procedure"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#" + key,
							json.getString(key) + "^^http://www.w3.org/2001/XMLSchema#string", systemTime);
					put(q);

					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("procedure"),
							"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#System", systemTime);
					put(q);
					break;

				case "hasSourceURI":
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("procedure"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#" + key,
							json.getString(key), systemTime);
					put(q);
					break;
				case "alt":
				case "lat":
				case "long":
					String processLocation = "http://knoesis.wright.edu/ssw/point_" + json.getString("ID");
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("procedure"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#processLocation",
							processLocation, systemTime);
					put(q);

					q = new RdfQuadruple(processLocation,
							"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
							"http://www.w3.org/2003/01/geo/wgs84_pos#Point", systemTime);
					put(q);
					q = new RdfQuadruple(processLocation,
							"http://www.w3.org/2003/01/geo/wgs84_pos#" + key,
							BigDecimal.valueOf(json.getDouble(key)).toString() + "^^http://www.w3.org/2001/XMLSchema#float", systemTime);
					put(q);
					break;
				case "distance":

					String hasLocatedNearRel = "http://knoesis.wright.edu/ssw/LocatedNearRel" + json.getString("ID");
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("procedure"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#hasLocatedNearRel",
							hasLocatedNearRel, systemTime);
					put(q);
					q = new RdfQuadruple(hasLocatedNearRel,
							"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#LocatedNearRel", systemTime);
					put(q);
					q = new RdfQuadruple(hasLocatedNearRel,
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#" + key,
							BigDecimal.valueOf(json.getDouble(key)).toString() + "^^http://www.w3.org/2001/XMLSchema#float", systemTime);
					put(q);
					break;
				case "hasLocation":
					String hasLocatedNearRel2 = "http://knoesis.wright.edu/ssw/LocatedNearRel" + json.getString("ID");
					q = new RdfQuadruple("http://knoesis.wright.edu/ssw/" + json.getString("procedure"),
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#hasLocatedNearRel",
							hasLocatedNearRel2, systemTime);
					put(q);
					q = new RdfQuadruple(hasLocatedNearRel2,
							"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#LocatedNearRel", systemTime);
					put(q);
					q = new RdfQuadruple(hasLocatedNearRel2,
							"http://knoesis.wright.edu/ssw/ont/sensor-observation.owl#" + key,
							json.getString(key), systemTime);
					put(q);
					break;

				}// End of switch (key)
			}
		}// end of for (int i = 0; i < array.length(); i++)
	}

}
