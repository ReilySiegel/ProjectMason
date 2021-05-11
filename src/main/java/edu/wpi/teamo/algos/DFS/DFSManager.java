package edu.wpi.teamo.algos.DFS;

import edu.wpi.teamo.algos.AlgoManagerI;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.algos.DFS.DFS;
import edu.wpi.teamo.database.map.IMapService;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Extends to AlgoManagerI(superclass) to avoid duplicate codes
 */
public class DFSManager extends AlgoManagerI {
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
