package edu.wpi.teamo.map.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Stream;

public class MapDB implements IMapService {
    Database db;

    public MapDB(String nodeCSVFilepath, String edgeCSVFilepath) throws FileNotFoundException, SQLException, ClassNotFoundException {

        /* read csv files into hashmaps */
        HashMap<String, Node> nodes = NodeCSV.read(nodeCSVFilepath);
        HashMap<String, Edge> edges = EdgeCSV.read(edgeCSVFilepath);

        /* initialize database */
        db = new Database();
        Node.initTable(db);
        Edge.initTable(db);

        /* save to the database */
        storeNodes(db, nodes);
        storeEdges(db, edges);

    }

    public MapDB(String databaseName) throws SQLException, ClassNotFoundException {
        /* derive init command from custom name */
        String uri = String.format("jdbc:derby:memory:%s;create=true", databaseName);
        db = new Database(uri);
        Node.initTable(db);
        Edge.initTable(db);
    }

    public MapDB() throws SQLException, ClassNotFoundException {
        db = new Database();
        Node.initTable(db);
        Edge.initTable(db);
    }

    public void loadEdgesFromFile(String filepath) throws FileNotFoundException, SQLException {
        /* read file into hashmap */
        HashMap<String, Edge> edges = EdgeCSV.read(filepath);
        /* save to the database */
        storeEdges(db, edges);
    }

    public void loadNodesFromFile(String filepath) throws FileNotFoundException, SQLException {
        /* read file into hashmap */
        HashMap<String, Node> nodes = NodeCSV.read(filepath);
        /* save to the database */
        storeNodes(db, nodes);
    }

    private static void storeNodes(Database db, HashMap<String, Node> nodes) throws SQLException {
        for (String key : nodes.keySet()) {
            Node node = nodes.get(key);
            node.update(db);
        }
    }

    private void storeEdges(Database db, HashMap<String, Edge> edges) throws SQLException {
        for (String key : edges.keySet()) {
            Edge edge = edges.get(key);
            edge.update(db);
        }
    }

    /**
     * Check if a specified node exists in the database
     * @param id Id of the node in question
     * @return True if node exists
     */
    public boolean nodeExists(String id) {
        try {
            Node.getByID(db, id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Check if a specified edge exists in the database
     * @param id Id of the edge in question
     * @return True if edge exists
     */
    public boolean edgeExists(String id) {
        try {
            Edge.getByID(db, id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void setNodePosition(String id, int newX, int newY) throws SQLException {
        /* get node object from database */
        Node node = Node.getByID(db, id);

        /* update node */
        node.setXPos(newX);
        node.setYPos(newY);

        /* update database */
        node.update(db);
    }

    public void setNodeLongName(String id, String newName) throws SQLException {
        /* get node object from database */
        Node node = Node.getByID(db, id);

        /* update node */
        node.setLongName(newName);

        /* update database */
        node.update(db);
    }

    public void setEdgeID(String oldID, String newID) throws SQLException {
        /* get edge object from database */
        Edge edge = Edge.getByID(db, oldID);

        /* delete from database */
        edge.delete(db);

        /* update node */
        edge.setEdgeID(newID);

        /* update database */
        edge.update(db);
    }

    @Override
    public void setEdgeStartID(String edgeID, String startNodeID) throws SQLException {
        /* get edge object from hashtable (we could get it from database but in this case its faster) */
        Edge edge = Edge.getByID(db, edgeID);

        /* update node */
        edge.setStartID(startNodeID);

        /* update database */
        edge.update(db);
    }

    @Override
    public void setEdgeEndID(String edgeID, String endNodeID) throws SQLException {
        /* get edge object from hashtable (we could get it from database but in this case its faster) */
        Edge edge = Edge.getByID(db, edgeID);

        /* update node */
        edge.setEndID(endNodeID);

        /* update database */
        edge.update(db);
    }

    @Override
    public Stream<NodeInfo> getAllNodes() throws SQLException {
        return Node.getAll(db).map((Node n) -> (NodeInfo) n);
    }

    @Override
    public Stream<EdgeInfo> getAllEdges() throws SQLException {
        return Edge.getAll(db).map((Edge e) -> (EdgeInfo) e);
    }

    public NodeInfo getNode(String id) throws SQLException {
        NodeInfo nodeInfo = Node.getByID(db, id);
        return nodeInfo;
    }

    public EdgeInfo getEdge(String id) throws SQLException {
        EdgeInfo edgeInfo = Edge.getByID(db, id);
        return edgeInfo;
    }

    public void addNode(String nodeID, int xPos, int yPos, String floor,
                        String building, String nodeType, String longName, String shortName) throws SQLException {

        Node n = new Node (nodeID, xPos, yPos, floor, building, nodeType, longName, shortName);
        n.update(db);
    }

    public void addEdge(String edgeID, String startNodeID, String endNodeID) throws SQLException {
        Edge e = new Edge(edgeID, startNodeID, endNodeID);
        e.update(db);
    }

    public void deleteNode(String id) throws SQLException {
        Node.getByID(db, id).delete(db);
    }

    public void deleteEdge(String id) throws SQLException {
        Edge.getByID(db, id).delete(db);
    }

    public void writeEdgesToCSV(String filepath) throws SQLException, IOException {
        Stream<Edge> edgeStream = Edge.getAll(db);
        EdgeCSV.write(filepath, edgeStream);
    }

    public void writeNodesToCSV(String filepath) throws SQLException, IOException {
        Stream<Node> nodeStream = Node.getAll(db);
        NodeCSV.write(filepath, nodeStream);
    }

    @Override
    public void closeConnection() throws SQLException {
        db.close();
    }
}
