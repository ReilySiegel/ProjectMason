package edu.wpi.teamo.database.map;

public interface NodeInfo extends Comparable<NodeInfo> {
    String getShortName();
    String getNodeType();
    String getBuilding();
    String getLongName();
    String getNodeID();
    String getFloor();
    int getYPos();
    int getXPos();
}
