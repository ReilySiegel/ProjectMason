package edu.wpi.teamo.algos;

import java.util.LinkedList;

public interface AStarService {
    public LinkedList<Object> getNodeIDLookUpTable();
    public AStar loadAStar();
    public LinkedList<Node> getPath(String StartID, String endID);

}
