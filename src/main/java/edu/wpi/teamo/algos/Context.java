package edu.wpi.teamo.algos;

import edu.wpi.teamo.algos.AStar.AStarManager;
import edu.wpi.teamo.algos.BFS.BFSManager;
import edu.wpi.teamo.algos.BestFirst.BestFirstManager;
import edu.wpi.teamo.algos.DFS.DFSManager;
import edu.wpi.teamo.algos.DFS.GreedyDFSManager;
import edu.wpi.teamo.algos.Dijkstra.DijkstraManager;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * The actual controller class in Strategy Design Pattern
 * getPath() method should be called using a Context instance
 */
public class Context {
    private IStrategyPathfinding pathfindingAlgo;

    /* i use the codes because the combo box selector needs strings */
    public static final String dfsCode = "Exercise Mode";
    public static final String bfsCode = "BFS";
    public static final String aStarCode = "AStar";
    public static final String bestFirstCode = "BestFirst";
    public static final String DijkstraCode = "Dijkstra";
    public static final String GreedyDFSCode = "Greedy DFS";

    private final BFSManager bfm;

    private final DFSManager dfm;

    private final AStarManager asm;

    private final BestFirstManager bsfm;

    private final DijkstraManager dkm;

    private final GreedyDFSManager greedyDFSManager;

    public Context(BFSManager bfm, DFSManager dfm, AStarManager asm, BestFirstManager bsfm, DijkstraManager dkm, GreedyDFSManager greedyDFSManager) {
        setPathfindingAlgo(asm);
        this.bfm = bfm;
        this.dfm = dfm;
        this.asm = asm;
        this.bsfm = bsfm;
        this.dkm = dkm;
        this.greedyDFSManager = greedyDFSManager;
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
            case bestFirstCode:
                setPathfindingAlgo(bsfm);
                break;
            case DijkstraCode:
                setPathfindingAlgo(dkm);
                break;
            case GreedyDFSCode:
                setPathfindingAlgo(greedyDFSManager);
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

