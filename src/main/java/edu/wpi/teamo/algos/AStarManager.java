package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.*;

/**
 * Extends to AlgoManagerI(superclass) to avoid duplicate codes
 */
public class AStarManager extends AlgoManagerI {
   public AStarManager(IMapService service){
       super(service);
   }

    @Override
    public LinkedList<AlgoNode> getPath(String startID, String endID) throws SQLException {
        conditionSetup();
        AStar aStar = new AStar(nodes, startID, endID);
        return aStar.findPath(startID,endID);
    }
}
