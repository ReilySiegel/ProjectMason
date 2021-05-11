package edu.wpi.teamo.algos.DFS;

import edu.wpi.teamo.algos.AlgoManagerI;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.algos.DFS.GreedyDFS;
import edu.wpi.teamo.database.map.IMapService;

import java.sql.SQLException;
import java.util.LinkedList;

public class GreedyDFSManager extends AlgoManagerI {
    public GreedyDFSManager(IMapService service){super(service);}

    @Override
    public LinkedList<AlgoNode> getPath(String startID, String endID) throws SQLException {
        conditionSetup();
        GreedyDFS greedyDFS = new GreedyDFS(nodes,startID,endID);
        return greedyDFS.gdfsFindPath();
    }
}
