package edu.wpi.teamo.algos;

import java.sql.SQLException;
import java.util.LinkedList;

public interface IStrategyPathfinding {
    public LinkedList<AlgoNode> getPath(String StartID, String endID) throws SQLException;
}
