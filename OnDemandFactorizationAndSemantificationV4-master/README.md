# OnDemandFactorizationAndSemantificationV4

Performs on-demand factorization and semantification of streaming data.

# Instructions to downloads

1. Download Node-Red flow

Download Node-Red Flow from https://github.com/farahkarim/NodeRedFlows/blob/master/OnDemandSemantification.json

2. Download and build customized-CSPARQL engine from https://github.com/farahkarim/CustomizedCSPARQL-engine-OnDFS.git to generate jar files.

3. Download project to run csparql queries using customized engine from https://github.com/farahkarim/OnDemandFactorizationAndSemantificationV4.git

4. Download sample data and queries from https://github.com/farahkarim/DatasetAndQueries


# Instructions to run project in eclipse environment

1. Open project OnDemandFactorizationAndSemantificationV4 in eclipse

2. Add jar files of CustomizedCSPARQL-engine-OnDFS to the project

3. Add the path to the downloaded sample data in Read a file node. Start the Node-Red flow by clicking on the insert record node

4. Run the Server class in OnDemandFactorizationAndSemantificationV4

5. Run the Client class in OnDemandFactorizationAndSemantificationV4 and give the path to the input CSPARQL query

