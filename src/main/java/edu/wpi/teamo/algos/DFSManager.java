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
public class DFSManager extends AlgoManagerI{
    public DFSManager(IMapService service){
        super(service);
    }

    @Override
    public LinkedList<AlgoNode> getPath(String startID, String endID) throws SQLException {
        conditionSetup();
        DFS DepthFirstSearch = new DFS(nodes, startID, endID);
        return DepthFirstSearch.findPath();

    }

    public LinkedList<AlgoNode> getIsolatedNodes() throws SQLException {
        conditionSetup();
        DFS DepthFirstSearch = new DFS(nodes);
        return DepthFirstSearch.seekForIsolatedNodes();
    }
}
