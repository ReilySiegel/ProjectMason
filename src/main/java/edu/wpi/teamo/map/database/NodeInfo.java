package edu.wpi.teamo.map.database;

public interface NodeInfo {
    String getShortName();
    String getNodeType();
    String getBuilding();
    String getLongName();
    String getNodeID();
    String getFloor();
    int getYPos();
    int getXPos();
}
