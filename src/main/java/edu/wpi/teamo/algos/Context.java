package edu.wpi.teamo.algos;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * The actual controller class in Strategy Design Pattern
 * getPath() method should be called using a Context instance
 */
public class Context {
    private IStrategyPathfinding pathfindingAlgo;

    public Context(IStrategyPathfinding pathfindingAlgo){
        this.pathfindingAlgo = pathfindingAlgo;
    }

    public void setPathfindingAlgo(IStrategyPathfinding pathfindingAlgo) {
        this.pathfindingAlgo = pathfindingAlgo;
    }

    // plz call this getPath()
    public LinkedList<AlgoNode> getPath(String startID, String EndID) throws SQLException {
        return pathfindingAlgo.getPath(startID, EndID);
    }
}
