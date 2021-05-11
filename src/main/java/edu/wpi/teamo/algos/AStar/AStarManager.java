package edu.wpi.teamo.algos.AStar;

import edu.wpi.teamo.algos.AStar.AStar;
import edu.wpi.teamo.algos.AlgoManagerI;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.database.map.*;
import java.sql.SQLException;
import java.util.*;

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
