package edu.wpi.teamo.algos;

import edu.wpi.teamo.algos.AStar.AStarManager;
import edu.wpi.teamo.algos.BFS.BFSManager;
import edu.wpi.teamo.algos.BestFirst.BestFirstManager;
import edu.wpi.teamo.algos.DFS.DFSManager;
import edu.wpi.teamo.algos.Dijkstra.DijkstraManager;
import edu.wpi.teamo.database.map.MapDB;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContextTests {

    @Test
    public void testGetPath() throws SQLException, ClassNotFoundException {
        // make to have different databaseName
        MapDB mdb = new MapDB("testContextFindPath");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","PARK","Floor1RightParking1","F1RightP1");
        mdb.addNode("oPARK00201", 3116,1155,"F1", "b","PARK","Floor1RightParking2","F1RightP2");
        mdb.addNode("oPARK00301", 3116,1181,"F1", "b","PARK","Floor1RightParking3","F1RightP3");
        mdb.addNode("oPARK00401", 3116,1207,"F1", "b","PARK","Floor1RightParking4","F1RightP4");
        mdb.addNode("oPARK00501", 3116,1233,"F1", "b","PARK","Floor1RightParking5","F1RightP5");
        mdb.addNode("oPARK00601", 3116,1253,"F1", "b","PARK","Floor1RightParking6","F1RightP6");
        mdb.addNode("oPARK00701", 3242,1111,"F1", "b","PARK","Floor1RightParking7","F1RightP7");
        mdb.addNode("oPARK00801", 3242,1133,"F1", "b","PARK","Floor1RightParking8","F1RightP8");
        mdb.addNode("oPARK00901", 3242,1157,"F1", "b","PARK","Floor1RightParking9","F1RightP9");
        mdb.addNode("oPARK01001",  3284,1279,"F1", "b","PARK","Floor1RightParkingA","F1RightPA");
        mdb.addNode("oPARK01101", 1234,1234, "F1", "b","PARK","Floor1RightParkingB","F1RightPB");
        mdb.addEdge("oPARK00101_oPARK00301","oPARK00101","oPARK00301");
        mdb.addEdge("oPARK00101_oPARK00601","oPARK00101","oPARK00601");
        mdb.addEdge("oPARK00101_oPARK00201","oPARK00101","oPARK00201");
        mdb.addEdge("oPARK00201_oPARK00701","oPARK00201","oPARK00701");
        mdb.addEdge("oPARK00601_oPARK00401","oPARK00601","oPARK00401");
        mdb.addEdge("oPARK00601_oPARK00501","oPARK00601","oPARK00501");
        mdb.addEdge("oPARK00701_oPARK00401","oPARK00701","oPARK00401");
        mdb.addEdge("oPARK00701_oPARK00801","oPARK00701","oPARK00801");
        mdb.addEdge("oPARK00801_oPARK00901","oPARK00801","oPARK00901");
        mdb.addEdge("oPARK00801_oPARK01001","oPARK00801","oPARK01001");


        // Contest using dFS
        DFSManager dfs = new DFSManager(mdb);
        Context context = new Context(null, dfs, null,null,null, null);
        context.setPathfindingAlgo(dfs);
        LinkedList<AlgoNode> path_temp = context.getPath("oPARK01001","oPARK00501");

        assertEquals(7, path_temp.size());
        assertEquals("oPARK01001", path_temp.get(0).getID());
        assertEquals("oPARK00801", path_temp.get(1).getID());
        assertEquals("oPARK00701", path_temp.get(2).getID());
        assertEquals("oPARK00201", path_temp.get(3).getID());
        assertEquals("oPARK00101", path_temp.get(4).getID());
        assertEquals("oPARK00601", path_temp.get(5).getID());
        assertEquals("oPARK00501", path_temp.get(6).getID());


        // Switching Algorithm by calling the setter
        // in this case, switch to Astar
        AStarManager asm = new AStarManager(mdb);
        context.setPathfindingAlgo(asm);
        LinkedList<AlgoNode> path_temp1 = context.getPath("oPARK01001","oPARK00501");

        assertEquals(6, path_temp1.size());
        assertEquals("oPARK01001", path_temp1.get(0).getID());
        assertEquals("oPARK00801", path_temp1.get(1).getID());
        assertEquals("oPARK00701", path_temp1.get(2).getID());
        assertEquals("oPARK00401", path_temp1.get(3).getID());
        assertEquals("oPARK00601", path_temp1.get(4).getID());
        assertEquals("oPARK00501", path_temp1.get(5).getID());


        // Switching Algorithms by calling the setter
        // in this case, switch to BFS
        BFSManager bfs = new BFSManager(mdb);
        context.setPathfindingAlgo(bfs);
        LinkedList<AlgoNode> path_bfs = context.getPath("oPARK01001","oPARK00501");

        assertEquals(6, path_bfs.size());
        assertEquals("oPARK01001", path_bfs.get(0).getID());
        assertEquals("oPARK00801", path_bfs.get(1).getID());
        assertEquals("oPARK00701", path_bfs.get(2).getID());
        assertEquals("oPARK00401", path_bfs.get(3).getID());
        assertEquals("oPARK00601", path_bfs.get(4).getID());
        assertEquals("oPARK00501", path_bfs.get(5).getID());

        // Switching Algorithms by calling the setter
        // in this case, switch to BestFirst
        BestFirstManager bestFirstManager = new BestFirstManager(mdb);
        context.setPathfindingAlgo(bestFirstManager);
        LinkedList<AlgoNode> path_BestFirst = context.getPath("oPARK01001","oPARK00501");

        assertEquals(6, path_BestFirst.size());
        assertEquals("oPARK01001", path_BestFirst.get(0).getID());
        assertEquals("oPARK00801", path_BestFirst.get(1).getID());
        assertEquals("oPARK00701", path_BestFirst.get(2).getID());
        assertEquals("oPARK00401", path_BestFirst.get(3).getID());
        assertEquals("oPARK00601", path_BestFirst.get(4).getID());
        assertEquals("oPARK00501", path_BestFirst.get(5).getID());

        // Switching Algorithms by calling the setter
        // in this case, switch to Dijkstra
        DijkstraManager djm = new DijkstraManager(mdb);
        context.setPathfindingAlgo(djm);
        LinkedList<AlgoNode> dijkstra_path = context.getPath("oPARK01001","oPARK00501");

        assertEquals(6, dijkstra_path.size());
        assertEquals("oPARK01001", dijkstra_path.get(0).getID());
        assertEquals("oPARK00801", dijkstra_path.get(1).getID());
        assertEquals("oPARK00701", dijkstra_path.get(2).getID());
        assertEquals("oPARK00401", dijkstra_path.get(3).getID());
        assertEquals("oPARK00601", dijkstra_path.get(4).getID());
        assertEquals("oPARK00501", dijkstra_path.get(5).getID());


        System.out.println("If reach here, tests for Context.getPath() are passed!! ");
    }

}
