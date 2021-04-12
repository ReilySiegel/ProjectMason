package edu.wpi.teamo.map.database;

public interface MapStorage {

    /* the getters */
    NodeInfo getNode(String id);
    EdgeInfo getEdge(String id);

    /* the setters */
    boolean setNodePosition(String id, int newX, int newY);
    boolean setNodeLongName(String id, String name);

    /* the makers */
    boolean addEdge(String edgeID, String startNodeID, String endNodeID);
    boolean addNode(String nodeID, int xPos, int yPos, String floor,
                    String building, String nodeType, String longName, String shortNam);

    /* the takers */
    boolean deleteNode(String id);
    boolean deleteEdge(String id);

}
