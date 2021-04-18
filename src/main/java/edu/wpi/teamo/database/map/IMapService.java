package edu.wpi.teamo.database.map;

import java.io.FileNotFoundException;
import java.util.stream.Stream;
import java.sql.SQLException;
import java.io.IOException;

public interface IMapService {

    /* the getters */
    Stream<NodeInfo> getAllNodes() throws SQLException;
    Stream<EdgeInfo> getAllEdges() throws SQLException;
    NodeInfo getNode(String id) throws SQLException;
    EdgeInfo getEdge(String id) throws SQLException;

    /* the csv loaders */
    void loadMapFromCSV(String filepath) throws IOException, SQLException;
    void loadNodesFromFile(String filepath) throws FileNotFoundException, SQLException;
    void loadEdgesFromFile(String filepath) throws FileNotFoundException, SQLException;

    /* the node setters */
    void setNodePosition(String id, int newX, int newY) throws SQLException;
    void setNodeShortName(String id, String shortName) throws SQLException;
    void setNodeBuilding(String id, String building) throws SQLException;
    void setNodeLongName(String id, String longName) throws SQLException;
    void setNodeFloor(String id, String floor) throws SQLException;
    void setNodeID(String oldID, String newID) throws SQLException;
    void setNodeType(String id, String type) throws SQLException;

    /* the edge setters */
    void setEdgeStartID(String edgeID, String startNodeID) throws SQLException;
    void setEdgeEndID(String edgeID, String endNodeID) throws SQLException;
    void setEdgeID(String oldID, String newID) throws SQLException;

    /* the makers */
    void writeNodesToCSV(String filepath) throws SQLException, IOException;
    void writeEdgesToCSV(String filepath) throws SQLException, IOException;
    void writeMapToCSV(String filepath) throws SQLException, IOException;
    void addEdge(String edgeID, String startNodeID, String endNodeID) throws SQLException;
    void addNode(String nodeID, int xPos, int yPos, String floor,
                 String building, String nodeType, String longName, String shortNam) throws SQLException;

    /* the takers */
    void deleteNode(String id) throws SQLException;
    void deleteEdge(String id) throws SQLException;
    void deleteMap() throws SQLException;

    /* the close */
    void closeConnection() throws SQLException;

}
