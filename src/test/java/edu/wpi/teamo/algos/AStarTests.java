package edu.wpi.teamo.algos;
import edu.wpi.teamo.map.database.Edge;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;
public class AStarTests {
    @Test
    public void AStarTest1()
    {
        // nodes
        Node p1 = new Node("oPARK00101", 3116,1131,"F1",NodeType.PARK,"Floor1RightParking1","F1RightP1");
        Node p2 = new Node("oPARK00201", 3116,1155,"F1",NodeType.PARK,"Floor1RightParking2","F1RightP2");
        Node p3 = new Node("oPARK00301", 3116,1181,"F1",NodeType.PARK,"Floor1RightParking3","F1RightP3");
        Node p4 = new Node("oPARK00401", 3116,1207,"F1",NodeType.PARK,"Floor1RightParking4","F1RightP4");
        Node p5 = new Node("oPARK00501", 3116,1233,"F1",NodeType.PARK,"Floor1RightParking5","F1RightP5");
        Node p6 = new Node("oPARK00601", 3116,1253,"F1",NodeType.PARK,"Floor1RightParking6","F1RightP6");
        Node p7 = new Node("oPARK00701", 3242,1111,"F1",NodeType.PARK,"Floor1RightParking7","F1RightP7");
        Node p8 = new Node("oPARK00801", 3242,1133,"F1",NodeType.PARK,"Floor1RightParking8","F1RightP8");
        Node p9 = new Node("oPARK00901", 3242,1157,"F1",NodeType.PARK,"Floor1RightParking9","F1RightP9");
        Node p10 = new Node("oPARK01001", 3284,1279,"F1",NodeType.PARK,"Floor1RightParking10","F1RightP10");

        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(p1);
        nodes.add(p2);
        nodes.add(p3);
        nodes.add(p4);
        nodes.add(p5);
        nodes.add(p6);
        nodes.add(p7);
        nodes.add(p8);
        nodes.add(p9);
        nodes.add(p10);


        // edges
        Edge P1_P3 = new Edge("oPARK00101_oPARK00301","oPARK00101","oPARK00301");
        Edge P1_P6 = new Edge("oPARK00101_oPARK00601","oPARK00101","oPARK00601");
        Edge P1_P2 = new Edge("oPARK00101_oPARK00201","oPARK00101","oPARK00201");
        Edge P2_P7 = new Edge("oPARK00201_oPARK00701","oPARK00201","oPARK00701");
        Edge P6_P4 = new Edge("oPARK00601_oPARK00401","oPARK00601","oPARK00401");
        Edge P6_P5 = new Edge("oPARK00601_oPARK00501","oPARK00601","oPARK00501");
        Edge P7_P4 = new Edge("oPARK00701_oPARK00401","oPARK00701","oPARK00401");
        Edge P7_P8 = new Edge("oPARK00701_oPARK00801","oPARK00701","oPARK00801");
        Edge P8_P9 = new Edge("oPARK00801_oPARK00901","oPARK00801","oPARK00901");
        Edge P8_P10 = new Edge("oPARK00801_oPARK01001","oPARK00801","oPARK01001");

        LinkedList<Edge> edges = new LinkedList<>();
        edges.add(P1_P3);
        edges.add(P1_P6);
        edges.add(P1_P2);
        edges.add(P2_P7);
        edges.add(P6_P4);
        edges.add(P6_P5);
        edges.add(P7_P4);
        edges.add(P7_P8);
        edges.add(P8_P9);
        edges.add(P8_P10);

        // Test for assignNodeAdjacency logics
        for(Node n: nodes){
            for (Edge e: edges){
                if (n.getID().equals(e.getStartNodeID())) n.addAdjacencyByNodeId(e.getEndNodeID());
                else if (n.getID().equals(e.getEndNodeID())) n.addAdjacencyByNodeId(e.getStartNodeID());
            }
        }

        AStar a = new AStar();
        a.setAllTheMess(nodes);

        LinkedList<Node> pathP1_P5 = a.findPath("oPARK00101","oPARK00501");

        LinkedList<Node> pathP1_P7 = a.findPath("oPARK00101","oPARK00701");

        LinkedList<Node> pathP1_P8 = a.findPath("oPARK00101","oPARK00801");

        LinkedList<Node> pathP8_P1 = a.findPath("oPARK00801","oPARK00101");

        LinkedList<Node> pathP6_P10 = a.findPath("oPARK00601","oPARK01001");

        LinkedList<Node> pathP5_P10 = a.findPath("oPARK00501","oPARK01001");

        LinkedList<Node> pathP10_P5 = a.findPath("oPARK01001","oPARK00501");

        for (Node n: pathP10_P5){
            System.out.println(n.getID()+"-->");
        }



    }

    @Test
    public void AStarTest2(){}


    public void AStarTest3(){}



}
