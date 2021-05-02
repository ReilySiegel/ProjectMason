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


    public LinkedList<AlgoNode> BestFirstFindPath(String startID, String endID){
        this.startID = startID;
        this.endID = endID;
        BestFirstPathFinding();
        LinkedList<AlgoNode> path = generatePath();
        return path;
    }


    private void BestFirstPathFinding(){

        AlgoNode startNode = nodeIDToAlgoNode(startID);
        AlgoNode endNode = nodeIDToAlgoNode(endID);

        LinkedList<AlgoNode> open = new LinkedList<>();
        LinkedList<AlgoNode> close = new LinkedList<>();

        open.add(startNode);

        while(!open.isEmpty()){

            AlgoNode currentNode = open.getFirst();

            for (AlgoNode n: open){
                if (isHCostLower(n,currentNode)){
                    currentNode = n;
                }
            }

            open.remove(currentNode);
            close.add(currentNode);

            if ((currentNode.getID() == endID) || currentNode == endNode)
                return;

            for(AlgoNode neighbor: adjacenciesToNodes(currentNode)){
                if (close.contains(neighbor))
                    continue;

                if(!open.contains(neighbor)){
                    neighbor.set_hCost(computeCost(neighbor,endNode));
                    neighbor.setParent(currentNode);
                    open.add(neighbor);
                }
            }
        }
    }

    /**
     * Returns the specified node's adjacent nodes
     * @param node the node from which to retrieve the adjacent nodes
     * @return The list of adjacent nodes
     */
    public LinkedList<AlgoNode> adjacenciesToNodes(AlgoNode node) {
        LinkedList<AlgoNode> list = new LinkedList<>();
        for (String s : node.getAdjacencies()) {
            for (AlgoNode n : nodes) {
                if (n.getID().equals(s)) list.add(n);
            }
        }
        return list;
    }

    private boolean isHCostLower(AlgoNode targetNode, AlgoNode currentNode){

        return targetNode.get_hCost() < currentNode.get_hCost();

    }

    private int computeCost(AlgoNode nodeA, AlgoNode nodeB){
        int nodeAXCoordinate = nodeA.getX();
        int nodeAYCoordinate = nodeA.getY();
        int nodeBXCoordinate = nodeB.getX();
        int nodeBYCoordinate = nodeB.getY();


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


    /**
     * Creates a path based using the list of nodes
     * @return a linked list of path from startNode ---> endNode
     */
    private LinkedList<AlgoNode> generatePath(){
        AlgoNode start = nodeIDToAlgoNode(startID);
        AlgoNode end = nodeIDToAlgoNode(endID);

        LinkedList<AlgoNode> path = new LinkedList<AlgoNode>();
        AlgoNode currentNode = end;

        while(currentNode != start){
            if (currentNode.getParent() == null){
                return path;
            }
             path.addFirst(currentNode);
             currentNode = currentNode.getParent();

        }



        path.addFirst(start);
        return path;
    }


}
