package windowBasedFactorization;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.larkc.csparql.cep.api.JSONStream;
import eu.larkc.csparql.core.engine.ConsoleFormatter;
import eu.larkc.csparql.core.engine.CsparqlEngineExt;
import eu.larkc.csparql.core.engine.CsparqlEngineExtImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;

public class ExecuteQuery {

	private static JSONStream jsonStream = null;
	private static CsparqlEngineExt engine = null;
	private ArrayList<String> queries = new ArrayList<String>();
	private static Logger logger = LoggerFactory
			.getLogger(ExecuteQuery.class);
	private String query;

	public ExecuteQuery(final ArrayList<String> queries) {
		if (engine == null) {
			// try {
			// PropertyConfigurator
			// .configure(new URL(
			// "http://streamreasoning.org/log4j.properties"));
			// } catch (final MalformedURLException e) {
			// logger.error(e.getMessage(), e);
			// }
			// Run it only once in the beginning only
			engine = new CsparqlEngineExtImpl();
			engine.initialize(true);
			logger.debug("CSparql Extended Engine Started.");
			jsonStream = new DataStreamGenerator("http://www.cwi.nl/SRBench/observations");
			engine.registerStream(jsonStream);
			final Thread t = new Thread((Runnable) jsonStream);
			t.start();
			// System.out.println("KG Thread Started...");
		}// End of if(engine==null)

		this.queries.addAll(queries);
	}// End of public ExecuteQuery(final ArrayList<String> queries)

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void runQueries() {
		Map<Integer, CsparqlQueryResultProxy> resultProxyList = new HashMap<Integer, CsparqlQueryResultProxy>();
		// Run this code for each query
		for (int i = 0; i < this.queries.size(); i++) {
			// Register a C-SPARQL query
			resultProxyList.put(i, null);
			// CsparqlQueryResultProxy c1 = null;
			// CsparqlQueryResultProxy c2 = null;
			try {
				// c1 = engine.registerQuery(query, false);
				CsparqlQueryResultProxy c = resultProxyList.get(i);
				c = engine.registerQuery(queries.get(i), false);
				/// get window size here
				resultProxyList.put(i, c);
				logger.debug("Query: {}", query);
				logger.debug("Query Start Time : {}",
						System.currentTimeMillis());
			} catch (final ParseException ex) {
				logger.error(ex.getMessage(), ex);
			}
			// Attach a Result Formatter to the query result proxy
			if (resultProxyList.get(i) != null) {
				resultProxyList.get(i).addObserver(new ConsoleFormatter());
			}// End of if (c1 != null)
		}// End of for(int i=0;i<queries.size();i++)
			// leave the system running for a while
			// normally the C-SPARQL Engine should be left running
			// the following code shows how to stop the C-SPARQL Engine gracefully
		try {
			// 20m = 1200000
			// 1h = 3600000
			Thread.sleep(3600000);
		} catch (final InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		// clean up (i.e., unregister query and stream)
		for (int i = 0; i < this.queries.size(); i++) {
			// engine.unregisterQuery(c1.getId());
			engine.unregisterQuery(resultProxyList.get(i).getId());
		}// End of for (int i = 0; i < this.queries.size(); i++)
			// ((FactorizedRDFDataStreamGenerator) knowledgeGraph).pleaseStop();
			// engine.unregisterStream(knowledgeGraph.getIRI());
		System.out.println("Query Execution stopped after 500000ms");
		System.exit(0);
	}// End of runQueries() function

}
