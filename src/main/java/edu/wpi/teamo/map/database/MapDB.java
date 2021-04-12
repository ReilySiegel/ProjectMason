package edu.wpi.teamo.map.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Stream;

public class MapDB implements IMapService {
    Database db;
    HashMap<String, Node> nodes;
    HashMap<String, Edge> edges;

    public MapDB(String nodeCSVFilepath, String edgeCSVFilepath) {

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
                node.set(db);

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
                node.set(db);

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
        //TODO: Finish
        return null;
    }

    @Override
    public Stream<EdgeInfo> getAllEdges() {
        //TODO: Finish
        return null;
    }

    public NodeInfo getNode(String id) {
        NodeInfo nodeInfo = null;
        //TODO: Finish
        return nodeInfo;
    }

    public EdgeInfo getEdge(String id) {
        EdgeInfo edgeInfo = null;
        //TODO: Finsh
        return edgeInfo;
    }

    public boolean addNode(String nodeID, int xPos, int yPos, String floor,
                           String building, String nodeType, String longName, String shortName) {
        boolean added = true;
        //TODO: Finsh
        return added;
    }

    public boolean addEdge(String edgeID, String startNodeID,  String endNodeID) {
        boolean added = true;
        //TODO: Finsh
        return added;
    }

    public boolean deleteNode(String id) {
        boolean deleted = true;
        //TODO: Finish
        return deleted;
    }

    public boolean deleteEdge(String id) {
        boolean deleted = true;
        //TODO: Finish
        return deleted;
    }

}