package edu.wpi.teamo.algos;

import edu.wpi.teamo.App;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * The actual controller class in Strategy Design Pattern
 * getPath() method should be called using a Context instance
 */
public class Context {
    private IStrategyPathfinding pathfindingAlgo;

    /* i use the codes because the combo box selector needs strings */
    public static final String dfsCode = "DFS";
    public static final String bfsCode = "BFS";
    public static final String aStarCode = "AStar";

    private final BFSManager bfm;

    private final DFSManager dfm;

    private final AStarManager asm;

    public Context(BFSManager bfm, DFSManager dfm, AStarManager asm) {
        setPathfindingAlgo(asm);
        this.bfm = bfm;
        this.dfm = dfm;
        this.asm = asm;
    }

    public void setPathfindingAlgo(IStrategyPathfinding pathfindingAlgo) {
        this.pathfindingAlgo = pathfindingAlgo;
    }

    public void switchAlgoManagerByCode(String code) {
        switch (code) {
            case dfsCode:
                setPathfindingAlgo(dfm);
                break;
            case bfsCode:
                setPathfindingAlgo(bfm);
                break;
            case aStarCode:
                setPathfindingAlgo(asm);
                break;
        }
    }

    // plz call this getPath()
    public LinkedList<AlgoNode> getPath(String startID, String EndID) throws SQLException {
        return pathfindingAlgo.getPath(startID, EndID);
    }

    public IStrategyPathfinding getAlgoManager() {
        return pathfindingAlgo;
    }
}

