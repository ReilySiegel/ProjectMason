package edu.wpi.teamo.algos;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
public class nodeTest {
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
        Node n = new Node(ID,x,y,f,t,ln,sn);
        assertEquals(1,n.getX());
    }


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
        Node n = new Node(ID,x,y,f,t,ln,sn);
        assertEquals(1,n.getY());

    }


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
        Node n = new Node(ID,x,y,f,t,ln,sn);
        assertEquals(t,n.getType());
    }

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
        Node n = new Node(ID,x,y,f,t,ln,sn);
        assertEquals(f,n.getFloor());
    }


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
        Node n = new Node(ID,x,y,f,t,ln,sn);
        assertEquals(ID,n.getID());

    }


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
        Node n = new Node(ID,x,y,f,t,ln,sn);
        assertEquals(ln,n.getLongName());

    }

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
        Node n = new Node(ID,x,y,f,t,ln,sn);
        assertEquals(sn,n.getShortName());

    }


    @Test
    public void adjacenciesTest()
    {
        // tells you what test is running
        System.out.println("testing the getAdjaciencies and addNodeByID MethodforSingleCase");
        // tells you the node details
        String ID = "n001";
        int x = 1;
        int y = 1;
        String f = "floor1";
        NodeType t = NodeType.REST;
        String ln = "nobody out pizzas the hut";
        String sn = "Jabba the hut";
        Node n = new Node(ID,x,y,f,t,ln,sn);
        //setting up the expected case
        String ajt = "n002";
        LinkedList<String> s = new LinkedList();
        s.addLast(ajt);
        n.addAdjacencyByNodeId("n002");
        // testing
        assertEquals(s,n.getAdjacencies());
    }
}
