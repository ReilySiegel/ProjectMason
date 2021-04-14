package edu.wpi.teamo.map.database;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

public interface IMapService {

    /* the getters */
    Stream<NodeInfo> getAllNodes();
    Stream<EdgeInfo> getAllEdges();
    NodeInfo getNode(String id);
    EdgeInfo getEdge(String id);

    /* the setters */
    void loadNodesFromFile(String filepath) throws FileNotFoundException;
    void loadEdgesFromFile(String filepath) throws FileNotFoundException;
    boolean setNodePosition(String id, int newX, int newY);
    boolean setNodeLongName(String id, String name);

    /* the makers */
    boolean writeNodesToCSV(String filepath);
    boolean writeEdgesToCSV(String filepath);
    boolean addEdge(String edgeID, String startNodeID, String endNodeID);
    boolean addNode(String nodeID, int xPos, int yPos, String floor,
                    String building, String nodeType, String longName, String shortNam);

    /* the takers */
    boolean deleteNode(String id);
    boolean deleteEdge(String id);

}
