package edu.wpi.teamo.map.database;

import java.io.FileNotFoundException;
import java.util.stream.Stream;
import java.sql.SQLException;
import java.io.IOException;

public interface IMapService {

    /* the getters */
    Stream<NodeInfo> getAllNodes();
    Stream<EdgeInfo> getAllEdges();
    NodeInfo getNode(String id);
    EdgeInfo getEdge(String id);

    /* the setters */
    void loadNodesFromFile(String filepath) throws FileNotFoundException, SQLException;
    void loadEdgesFromFile(String filepath) throws FileNotFoundException, SQLException;
    boolean setNodePosition(String id, int newX, int newY) throws SQLException;
    void setEdgeStartID(String edgeID, String startNodeID) throws SQLException;
    void setEdgeEndID(String edgeID, String endNodeID) throws SQLException;
    void setNodeLongName(String id, String name) throws SQLException;
    void setEdgeID(String oldID, String newID) throws SQLException;

    /* the makers */
    void writeNodesToCSV(String filepath) throws SQLException, IOException;
    void writeEdgesToCSV(String filepath) throws SQLException, IOException;
    void addEdge(String edgeID, String startNodeID, String endNodeID) throws SQLException;
    void addNode(String nodeID, int xPos, int yPos, String floor,
                 String building, String nodeType, String longName, String shortNam) throws SQLException;

    /* the takers */
    void deleteNode(String id) throws SQLException;
    void deleteEdge(String id) throws SQLException;

    /* the close */
    void closeConnection() throws SQLException;

}
