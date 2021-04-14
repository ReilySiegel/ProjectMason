package edu.wpi.teamo.algos;

import java.util.LinkedList;

public interface AStarService {
    public LinkedList<AlgoNode> getPath(String StartID, String endID);
}
