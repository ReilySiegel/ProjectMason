package edu.wpi.teamo.algos;

import java.util.LinkedList;

public class BestFirst {

    private String startID;
    private String endID;
    private LinkedList<AlgoNode> nodes;

    public BestFirst(LinkedList<AlgoNode> nodes, String startID, String endID){
        this.startID = startID;
        this.endID = endID;
        this.nodes = nodes;
    }



    private int computeCost(String nodeA, String nodeB){
        int nodeAXCoordinate = nodeIDToAlgoNode(nodeA).getX();
        int nodeAYCoordinate = nodeIDToAlgoNode(nodeA).getY();
        int nodeBXCoordinate = nodeIDToAlgoNode(nodeB).getX();
        int nodeBYCoordinate = nodeIDToAlgoNode(nodeB).getY();

        int xDifference = nodeBXCoordinate - nodeAXCoordinate;
        int yDifference = nodeBYCoordinate - nodeAYCoordinate;

        return (int)Math.sqrt(xDifference*xDifference + yDifference*yDifference);
    }

    private AlgoNode nodeIDToAlgoNode(String ID){
        try {
            for (AlgoNode n : nodes) {
                if (ID.equals(n.getID())) {
                    return n;
                }
            }
        }
        catch (Exception e){
            AlgoNode unknownNode = new AlgoNode(ID, 1000,1000);
            return unknownNode;
        }
        return null;
    }

}
