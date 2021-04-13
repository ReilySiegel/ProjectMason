package edu.wpi.teamo.map.database;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Stream;

public class MapDB implements IMapService {
    Database db;
    HashMap<String, Node> nodes;
    HashMap<String, Edge> edges;

    public MapDB(String nodeCSVFilepath, String edgeCSVFilepath) throws FileNotFoundException {

        /* read csv files into hashmaps */
        nodes = NodeCSV.read(nodeCSVFilepath);
        edges = EdgeCSV.read(edgeCSVFilepath);

        /* initialize database */
        initDB();
        Node.initTable(db);
        Edge.initTable(db);

        /* save to the database */
        storeNodes(db, nodes);
        storeEdges(db, edges);

    }

    private boolean initDB() {
        boolean connected = true;
        try {
            db = new Database();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("ERROR - COULD NOT INIT DATABASE");
            e.printStackTrace();
            connected = false;
        }
        return connected;
    }

    private static void storeNodes(Database db, HashMap<String, Node> nodes) {

        for (String key : nodes.keySet()) {
            Node node = nodes.get(key);

            try {
                node.update(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void storeEdges(Database db, HashMap<String, Edge> edges) {

        for (String key : edges.keySet()) {
            Edge edge = edges.get(key);

            try {
                edge.update(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Check if a specified node exists in the database
     * @param id Id of the node in question
     * @return True if node exists
     */
    public boolean nodeExists(String id) {
        boolean exists = true;
        try {
            Node.getByID(db, id);
        } catch (SQLException e) {
            exists = false;
        }
        return exists;
    }

    public boolean setNodePosition(String id, int newX, int newY) {
        boolean wasUpdated = true;
        if (nodeExists(id)) {
            try {

                /* get node object from hashtable (we could get it from database but in this case its faster) */
                Node node = nodes.get(id);

                /* update node */
                node.setXPos(newX);
                node.setYPos(newY);

                /* update database */
                node.update(db);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                wasUpdated = false;
            }
        }
        else { //if node doesnt exist
            wasUpdated = false;
        }
        return wasUpdated;
    }

    public boolean setNodeLongName(String id, String newName) {
        boolean wasUpdated = true;
        if (nodeExists(id)) {
            try {

                /* get node object from hashtable (we could get it from database but in this case its faster) */
                Node node = nodes.get(id);

                /* update node */
                node.setLongName(newName);

                /* update database */
                node.update(db);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                wasUpdated = false;
            }
        }
        else { //if node doesnt exist
            wasUpdated = false;
        }
        return wasUpdated;
    }

    @Override
    public Stream<NodeInfo> getAllNodes() {
        return nodes.values().stream().map((Node n) -> (NodeInfo) n);
    }

    @Override
    public Stream<EdgeInfo> getAllEdges() {
        return edges.values().stream().map((Edge e) -> (EdgeInfo) e);
    }

    public NodeInfo getNode(String id) {
        NodeInfo nodeInfo = nodes.get(id);
        return nodeInfo;
    }

    public EdgeInfo getEdge(String id) {
        EdgeInfo edgeInfo = edges.get(id);
        return edgeInfo;
    }

    public boolean addNode(String nodeID, int xPos, int yPos, String floor,
                           String building, String nodeType, String longName, String shortName) {

        Node n = new Node (nodeID, xPos, yPos, floor, building, nodeType, longName, shortName);
        try {
            n.update(db);
            nodes.put(nodeID, n);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean addEdge(String edgeID, String startNodeID,  String endNodeID) {
        Edge e = new Edge(edgeID, startNodeID, endNodeID);
        try {
            e.update(db);
            edges.put(edgeID, e);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean deleteNode(String id) {
        try {
            nodes.get(id).delete(db);
            nodes.remove(id);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean deleteEdge(String id) {
        try {
            edges.get(id).delete(db);
            edges.remove(id);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

}
