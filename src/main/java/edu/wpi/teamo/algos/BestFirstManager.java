package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.IMapService;

import java.sql.SQLException;
import java.util.LinkedList;

public class BestFirstManager extends AlgoManagerI{

    public BestFirstManager(IMapService service){super(service);}

    @Override
    public LinkedList<AlgoNode> getPath(String startID, String endID) throws SQLException {
        conditionSetup();
        BestFirst BestFirstSearch = new BestFirst(nodes,startID,endID);
        return BestFirstSearch.BestFirstFindPath(startID, endID);
    }
}
