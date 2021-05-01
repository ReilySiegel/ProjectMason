package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.*;

/**
 * Extends to AlgoManagerI(superclass) to avoid duplicate codes
 */
public class DijkstraManager extends AlgoManagerI {
    public DijkstraManager(IMapService service){
        super(service);
    }

    @Override
    public LinkedList<AlgoNode> getPath(String startID, String endID) throws SQLException {
        conditionSetup();
        Dijkstra dijkstra = new Dijkstra(nodes, startID, endID);
        return dijkstra.findPath(startID,endID);
    }
}
