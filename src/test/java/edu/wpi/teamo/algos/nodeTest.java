package edu.wpi.teamo.algos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class nodeTest {
    /**
     * Test for retrieving x coordinate of node
     */
    @Test
    public void getXTest()
    {
        System.out.println("running getX test when correct test");
        String ID = "n001";
        int x = 1;
        int y = 1;
        String f = "floor1";
        NodeType t = NodeType.REST;
        String ln = "nobody out pizzas the hut";
        String sn = "Jabba the hut";
        AlgoNode n = new AlgoNode(ID,x,y,f,t,ln,sn);
        assertEquals(1,n.getX());
    }

    /**
     * Test for retrieving y coordinate of node
     */
    @Test
    public void getYTest()
    {
        System.out.println("running getY test when correct test");
        String ID = "n001";
        int x = 1;
        int y = 1;
        String f = "floor1";
        NodeType t = NodeType.REST;
        String ln = "nobody out pizzas the hut";
        String sn = "Jabba the hut";
        AlgoNode n = new AlgoNode(ID,x,y,f,t,ln,sn);
        assertEquals(1,n.getY());

    }

    /**
     * Test for retrieving node type of the node
     */
    @Test
    public void getTypeTest()
    {
        System.out.println("running the getType test for when equal");
        String ID = "n001";
        int x = 1;
        int y = 1;
        String f = "Floor1";
        NodeType t = NodeType.REST;
        String ln = "nobody out pizzas the hut";
        String sn = "Jabba the hut";
        AlgoNode n = new AlgoNode(ID,x,y,f,t,ln,sn);
        assertEquals(t,n.getType());
    }

    /**
     * Test for retrieving the floor identifier of the node
     */
    @Test
    public void getFloorTest()
    {
        System.out.println("running the getfloor test for when equal");
        String ID = "n001";
        int x = 1;
        int y = 1;
        String f = "Floor1";
        NodeType t = NodeType.REST;
        String ln = "nobody out pizzas the hut";
        String sn = "Jabba the hut";
        AlgoNode n = new AlgoNode(ID,x,y,f,t,ln,sn);
        assertEquals(f,n.getFloor());
    }

    /**
     * Test for retrieving node ID of the node
     */
    @Test
    public void getIDTest()
    {
        System.out.println("running the getID test when equivalent");
        String ID = "n001";
        int x = 1;
        int y = 1;
        String f = "floor1";
        NodeType t = NodeType.REST;
        String ln = "nobody out pizzas the hut";
        String sn = "Jabba the hut";
        AlgoNode n = new AlgoNode(ID,x,y,f,t,ln,sn);
        assertEquals(ID,n.getID());

    }

    /**
     * Test for retrieving node's long name attribute
     */
    @Test
    public void getLongNameTest()
    {
        System.out.println("running the getLongNameTest for when answers are equivalent");
        String ID = "n001";
        int x = 1;
        int y = 1;
        String f = "floor1";
        NodeType t = NodeType.REST;
        String ln = "nobody out pizzas the hut";
        String sn = "Jabba the hut";
        AlgoNode n = new AlgoNode(ID,x,y,f,t,ln,sn);
        assertEquals(ln,n.getLongName());

    }

    /**
     * Test for retrieving node's short name attribute
     */
    @Test
    public void getshortNameTest()
    {
        System.out.println(" running the short Name test for when answers are equivalent");
        String ID = "n001";
        int x = 1;
        int y = 1;
        String f = "floor1";
        NodeType t = NodeType.REST;
        String ln = "nobody out pizzas the hut";
        String sn = "Jabba the hut";
        AlgoNode n = new AlgoNode(ID,x,y,f,t,ln,sn);
        assertEquals(sn,n.getShortName());

    }

}
