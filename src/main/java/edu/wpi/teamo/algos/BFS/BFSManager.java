package edu.wpi.teamo.algos.BFS;

import edu.wpi.teamo.algos.AlgoManagerI;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.algos.BFS.BFS;
import edu.wpi.teamo.database.map.IMapService;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Extends to AlgoManagerI(superclass) to avoid duplicate codes
 */
public class BFSManager extends AlgoManagerI {
    public BFSManager(IMapService service){
        super(service);
    }
    @Override
    public LinkedList<AlgoNode> getPath(String StartID, String endID) throws SQLException {
        conditionSetup();

        BFS BreadthFirstSearch = new BFS(nodes, StartID, endID);
        BreadthFirstSearch.setNodes(nodes);

        LinkedList<AlgoNode> path = BreadthFirstSearch.findPath(StartID, endID);

        return path;
    }
}
