package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.Edge;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;

public class AStarTests {
    /**
     * Test for AStar finding an arbitrary path
     *
    */
    @Test
    public void AStarTest1()
    {
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
        LinkedList<Edge> edges = new LinkedList<>();
        edges.add(new Edge("oPARK00101_oPARK00301","oPARK00101","oPARK00301"));
        edges.add(new Edge("oPARK00101_oPARK00601","oPARK00101","oPARK00601"));
        edges.add(new Edge("oPARK00101_oPARK00201","oPARK00101","oPARK00201"));


        // Test for assignNodeAdjacency logics
        AStarManager.assignNodeAdjacency(nodes, edges);

        AStar a = new AStar(nodes, edges, "","");
        a.setAllTheNodes(nodes);

        /**
         * Test for helper function adjacenciesToNodes(AlgoNode node)
         * checking all the adjacent nodes for n1
         */

        LinkedList<AlgoNode> adjacentNodesForN1 = a.adjacenciesToNodes(n1);
        LinkedList<AlgoNode> adjacentNodesForN1Expected = new LinkedList<>();
        adjacentNodesForN1Expected.add(n2);
        adjacentNodesForN1Expected.add(n3);
        adjacentNodesForN1Expected.add(n6);

        assertEquals(adjacentNodesForN1.size(),adjacentNodesForN1Expected.size());
        assertTrue(adjacentNodesForN1.contains(n2));
        assertTrue(adjacentNodesForN1Expected.contains(n2));
        assertTrue(adjacentNodesForN1.contains(n3));
        assertTrue(adjacentNodesForN1Expected.contains(n3));
        assertTrue(adjacentNodesForN1.contains(n6));
        assertTrue(adjacentNodesForN1Expected.contains(n6));

        System.out.println("If reach here, tests for Astar.adjacenciesToNodes() are passed!");

    }

    /**
     * this test case test the condition where n's fcost is less than m fcost
     */
    @Test
    public void testLowerFCost()
    {

        AlgoNode n = new AlgoNode("n001",2,3);
        n.set_gCost(1);
        n.set_hCost(2);
        AlgoNode m = new AlgoNode("n002",3,2);
        m.set_gCost(1);
        m.set_hCost(3);
        LinkedList<AlgoNode> l = new LinkedList<>();
        l.addLast(n);
        l.addLast(m);
        AStar a = new AStar(l,null,"n001","n001");
        assertTrue(a.lowerFcost(n, m));
        System.out.println("If reach here, test 1 for Astar.lowerFcost() is passed!");
    }

    /**
     * Test when n's gCost is higher than m's gCost
     */
    @Test
    public void testLowerFCost2()
    {
       AlgoNode n = new AlgoNode("n001",2,3);
       n.set_gCost(2);
       n.set_hCost(2);
       AlgoNode m = new AlgoNode("n002",3,2);
        m.set_gCost(1);
        m.set_hCost(3);
        LinkedList<AlgoNode> l = new LinkedList<>();
        l.addLast(n);
        l.addLast(m);
       AStar a = new AStar(l,null,"n001","n001");
        assertTrue(a.lowerFcost(n, m));
        System.out.println("If reach here, test 2 for Astar.lowerFcost() is passed!");
    }

    /**
     * Test for n and m having equal gcosts and hcosts
     */
    @Test
    public void testLowerFCost3()
    {
      AlgoNode n = new AlgoNode("n001",2,3);
      n.set_gCost(1);
      n.set_hCost(2);
      AlgoNode m = new AlgoNode("n002",3,2);
      m.set_gCost(1);
      m.set_hCost(2);
      LinkedList<AlgoNode> l = new LinkedList<>();
      l.addLast(n);
      l.addLast(m);
      AStar a = new AStar(l,null,"n001","n001");
      assertFalse(a.lowerFcost(n, m));
      System.out.println("If reach here, test 3 for Astar.lowerFcost() is passed!");
    }



    /**
     * False test case for isEnd function
     */
    @Test
    public void testisEND1()
    {
        AlgoNode n = new AlgoNode("n001",1,1);
        AlgoNode m = new AlgoNode("n002",1,2);
        LinkedList<AlgoNode> a = new LinkedList<>();
        a.addLast(n);
        a.addLast(m);
        AStar star = new AStar(a,null,"n001","n002");
        assertFalse(star.isEnd(n));
        System.out.println("If reach here, test 1 for Astar.isEnd() is passed!");
    }

    /**
     * True test case for isEnd function
     */
    @Test
    public void testisEND2()
    {
        AlgoNode n = new AlgoNode("n001",1,1);
        AlgoNode m = new AlgoNode("n002",1,2);
        LinkedList<AlgoNode> a = new LinkedList<>();
        a.addLast(n);
        a.addLast(m);
        AStar star = new AStar(a,null,"n001","n002");
        assertTrue(star.isEnd(m));
        System.out.println("If reach here, test 2 for Astar.isEnd() is passed!");
    }
}
