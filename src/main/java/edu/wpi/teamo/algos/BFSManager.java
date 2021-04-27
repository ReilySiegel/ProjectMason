package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.Edge;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.IMapService;
import edu.wpi.teamo.database.map.NodeInfo;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Extends to AlgoManagerI(superclass) to avoid duplicate codes
 */
public class BFSManager extends AlgoManagerI{
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
