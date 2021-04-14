package edu.wpi.teamo.map.database;

import java.io.FileNotFoundException;
import java.sql.SQLException;
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
    void loadNodesFromFile(String filepath) throws FileNotFoundException;
    void loadEdgesFromFile(String filepath) throws FileNotFoundException;
    boolean setNodePosition(String id, int newX, int newY) throws SQLException;
    void setNodeLongName(String id, String name) throws SQLException;

    /* the makers */
    void writeNodesToCSV(String filepath) throws SQLException, IOException;
    void writeEdgesToCSV(String filepath) throws SQLException, IOException;
    void addEdge(String edgeID, String startNodeID, String endNodeID) throws SQLException;
    void addNode(String nodeID, int xPos, int yPos, String floor,
                 String building, String nodeType, String longName, String shortNam) throws SQLException;

    /* the takers */
    void deleteNode(String id) throws SQLException;
    void deleteEdge(String id) throws SQLException;

}
