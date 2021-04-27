package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.*;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AStarManagerTests {

    /**
     * Test for AStarManager conducting a search for an arbitrary path
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     */
    @Test
    public void testFindPath() throws SQLException, ClassNotFoundException {

        MapDB mdb = new MapDB("testAStarFindPath");
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

        AStarManager asm = new AStarManager(mdb);
        LinkedList<AlgoNode> pathP10_P5 = asm.getPath("oPARK01001","oPARK00501");

        assertEquals(6, pathP10_P5.size());
        assertEquals("oPARK01001", pathP10_P5.get(0).getID());
        assertEquals("oPARK00801", pathP10_P5.get(1).getID());
        assertEquals("oPARK00701", pathP10_P5.get(2).getID());
        assertEquals("oPARK00401", pathP10_P5.get(3).getID());
        assertEquals("oPARK00601", pathP10_P5.get(4).getID());
        assertEquals("oPARK00501", pathP10_P5.get(5).getID());
        System.out.println("If reach here, tests for AstarManager.getPath() are passed!");
    }

    /**
     * Test for finding the trivial path (starting and ending nodes are the same)
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     */
    @Test
    public void testFindTrivialPath() throws SQLException, ClassNotFoundException  {
        MapDB mdb = new MapDB("testFindTrivialPath");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","PARK","Floor1RightParking1","F1RightP1");
        AStarManager asm = new AStarManager(mdb);
        LinkedList<AlgoNode> path = asm.getPath("oPARK00101","oPARK00101");
        assertEquals(1, path.size());
        assertEquals("oPARK00101", path.get(0).getID());
        System.out.println("If reach here, tests for testFindTrivialPath() are passed!");
    }

    /**
     * Test for case where there is no path (return just the starting node)
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     * @throws NullPointerException No path present
     */
    @Test
    public void testNoPath() throws SQLException, ClassNotFoundException, NullPointerException {
        MapDB mdb = new MapDB("testNoPath");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","PARK","Floor1RightParking1","F1RightP1");
        mdb.addNode("oPARK00201", 3116,1155,"F1", "b","PARK","Floor1RightParking2","F1RightP2");
        mdb.addNode("oPARK00301", 3116,1181,"F1", "b","PARK","Floor1RightParking3","F1RightP3");
        mdb.addEdge("oPARK00101_oPARK00301","oPARK00101","oPARK00301");
        AStarManager asm = new AStarManager(mdb);
        //Refactor AStar to handle exception internally?
        assertThrows(NullPointerException.class, () -> asm.getPath("oPARK00101","oPARK00201"));
        System.out.println("If reach here, tests for testNoPath() are passed!");
    }

    /**
     * Test for assignNodeAdjacency
     */
    @Test
    public void testAssignNodeAdjacency() {
        LinkedList<AlgoNode> nodes = new LinkedList<>();
        List<EdgeInfo> edges = new LinkedList<>();
        AlgoNode n1 = new AlgoNode("oPARK00101", 3116, 1131,"F1", NodeType.PARK,"Floor1RightParking1","F1RightP1");
        AlgoNode n2 = new AlgoNode("oPARK00102", 3116, 1151,"F1", NodeType.PARK,"Floor1RightParking2","F1RightP2");
        AlgoNode n3 = new AlgoNode("oPARK00301", 3116,1181,"F1", NodeType.PARK,"Floor1RightParking3","F1RightP3");
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        Edge e = new Edge("oPARK00101_oPARK00301","oPARK00101","oPARK00301");
        edges.add(e);
        AStarManager.assignNodeAdjacency(nodes, edges);
        assertEquals(n3.getID(), n1.getAdjacencies().get(0));
        assertEquals(n1.getID(), n3.getAdjacencies().get(0));
        assertEquals(0, n2.getAdjacencies().size());
        System.out.println("If reach here, test 1 for AstarManager.assignNodeAdjacency() is passed!");
    }

    @Test
    public void testAssignNodeAdjacency2(){
        // nodes
        LinkedList<AlgoNode> nodes = new LinkedList<>();
        AlgoNode n1 = new AlgoNode("oPARK00101", 3116,1131,"F1",NodeType.PARK,"Floor1RightParking1","F1RightP1");
        nodes.add(n1);
        AlgoNode n2 = new AlgoNode("oPARK00201", 3116,1155,"F1",NodeType.PARK,"Floor1RightParking2","F1RightP2");
        nodes.add(n2);
        AlgoNode n3 = new AlgoNode("oPARK00301", 3116,1181,"F1",NodeType.PARK,"Floor1RightParking3","F1RightP3");
        nodes.add(n3);
        AlgoNode n4 = new AlgoNode("oPARK00401", 3116,1207,"F1",NodeType.PARK,"Floor1RightParking4","F1RightP4");
        nodes.add(n4);
        AlgoNode n5 = new AlgoNode("oPARK00501", 3116,1233,"F1",NodeType.PARK,"Floor1RightParking5","F1RightP5");
        nodes.add(n5);
        AlgoNode n6 = new AlgoNode("oPARK00601", 3116,1253,"F1",NodeType.PARK,"Floor1RightParking6","F1RightP6");
        nodes.add(n6);
        AlgoNode n7 = new AlgoNode("oPARK00701", 3242,1111,"F1",NodeType.PARK,"Floor1RightParking7","F1RightP7");
        nodes.add(n7);
        AlgoNode n8 = new AlgoNode("oPARK00801", 3242,1133,"F1",NodeType.PARK,"Floor1RightParking8","F1RightP8");
        nodes.add(n8);
        AlgoNode n9 = new AlgoNode("oPARK00901", 3242,1157,"F1",NodeType.PARK,"Floor1RightParking9","F1RightP9");
        nodes.add(n9);
        AlgoNode n10 = new AlgoNode("oPARK01001", 3284,1279,"F1",NodeType.PARK,"Floor1RightParking10","F1RightP10");
        nodes.add(n10);

        // edges
        List<EdgeInfo> edges = new LinkedList<>();
        edges.add(new Edge("oPARK00101_oPARK00301","oPARK00101","oPARK00301"));
        edges.add(new Edge("oPARK00101_oPARK00601","oPARK00101","oPARK00601"));
        edges.add(new Edge("oPARK00101_oPARK00201","oPARK00101","oPARK00201"));
        edges.add(new Edge("oPARK00201_oPARK00701","oPARK00201","oPARK00701"));
        edges.add(new Edge("oPARK00601_oPARK00401","oPARK00601","oPARK00401"));
        edges.add(new Edge("oPARK00601_oPARK00501","oPARK00601","oPARK00501"));
        edges.add(new Edge("oPARK00701_oPARK00401","oPARK00701","oPARK00401"));
        edges.add(new Edge("oPARK00701_oPARK00801","oPARK00701","oPARK00801"));
        edges.add(new Edge("oPARK00801_oPARK00901","oPARK00801","oPARK00901"));
        edges.add(new Edge("oPARK00801_oPARK01001","oPARK00801","oPARK01001"));

        // Test for assignNodeAdjacency logics
        AStarManager.assignNodeAdjacency(nodes, edges);

        AStar a = new AStar(nodes,"","");
        a.setAllTheNodes(nodes);

        LinkedList<AlgoNode> pathP10_P5 = a.findPath("oPARK01001","oPARK00501");

        assertEquals(6, pathP10_P5.size());
        assertEquals(n10, pathP10_P5.get(0));
        assertEquals(n8, pathP10_P5.get(1));
        assertEquals(n7, pathP10_P5.get(2));
        assertEquals(n4, pathP10_P5.get(3));
        assertEquals(n6, pathP10_P5.get(4));
        assertEquals(n5, pathP10_P5.get(5));

        System.out.println("If reach here, test 2 for AstarManager.assignNodeAdjacency() is passed!");
    }


    /**
     * Test for invalid node type
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     */
    @Test
    public void testInvalidNodeType() throws SQLException, ClassNotFoundException {
        MapDB mdb = new MapDB("testInvalidNodeType");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","an invalid node type","Floor1RightParking1","F1RightP1");
        mdb.addNode("oPARK00201", 3116,1155,"F1", "b","another node type that is invalid","Floor1RightParking2","F1RightP2");
        mdb.addEdge("oPARK00101_oPARK00201","oPARK00101","oPARK00201");
        AStarManager asm = new AStarManager(mdb);
        LinkedList<AlgoNode> path = asm.getPath("oPARK00101","oPARK00201");
        assertEquals(NodeType.NULL, path.get(0).getType());
    }

}
