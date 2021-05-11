package edu.wpi.teamo.algos;

import java.util.Hashtable;
import java.util.LinkedList;

public class GreedyDFS {
    private LinkedList<AlgoNode> allNodes;
    private String startID;
    private String endID;
    private boolean[] marked;
    private LinkedList<String>[] adj; // adjacency lists
    private Hashtable<String, Integer> position;

    public GreedyDFS(LinkedList<AlgoNode> nodes, String startNodeID, String endNodeID){
        this.allNodes = nodes;
        this.startID = startNodeID;
        this.endID = endNodeID;
        this.marked = new boolean[nodes.size()];
        this.adj = (LinkedList<String>[]) new  LinkedList[nodes.size()];
        this.position = new Hashtable<>();

        int i = 0;
        for (AlgoNode n: nodes){
            adj[i] = n.getAdjacencies();
            position.put(n.getID(),i);
            i++;
        }

        gdfs(startNodeID);
    }

    public LinkedList<AlgoNode> gdfsFindPath(){
        LinkedList<AlgoNode> path = new LinkedList<AlgoNode>();
        path = generatePath();
        return path;
    }

    private void gdfs(String targetNode) {

        int targetIndex = position.get(targetNode);

        marked[targetIndex] = true;

        if (targetNode == endID) return;

        for (String w: adj[targetIndex]){
            try{

                AlgoNode current = stringToNode(targetNode);
                AlgoNode neighbor = stringToNode(w);

                int newMovementCostToNeighbor = current.get_gCost() + getDistance(current,neighbor);

                if (!marked[position.get(w)] || newMovementCostToNeighbor < neighbor.get_gCost()){
                    neighbor.set_gCost(newMovementCostToNeighbor);
                    stringToNode(w).setParent(stringToNode(targetNode));
                    gdfs(w);
                }
            }
            catch(NullPointerException e){
                System.out.println("null wus here: " + w);
            }
        }
    }


    /**
     * Creates a path based using the list of nodes
     * @return a linked list of path from startNode ---> endNode
     */
    private LinkedList<AlgoNode> generatePath(){
        AlgoNode start = stringToNode(startID);
        AlgoNode end = stringToNode(endID);

        LinkedList<AlgoNode> path = new LinkedList<AlgoNode>();
        AlgoNode currentNode = end;

        while(currentNode != start){
            path.addFirst(currentNode);
            currentNode = currentNode.getParent();
        }

        path.addFirst(start);
        return path;
    }


    /**
     * Returns the AlgoNode associated with the ID
     * @param ID the ID of the node to retrieve
     * @return the AlgoNode associated with the ID
     */
    private AlgoNode stringToNode(String ID){
        try {
            for (AlgoNode n : allNodes) {
                if (ID.equals(n.getID())) {
                    return n;
                }
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }

    /**
     * Get distance between two nodes using distance formula
     * @param nodeA (starting AlgoNode)
     * @param nodeB (target AlgoNode)
     * @return The distance between the two nodes
     */
    private int getDistance(AlgoNode nodeA, AlgoNode nodeB){
        int nodeAXCoordinate = nodeA.getX();
        int nodeAYCoordinate = nodeA.getY();
        int nodeBXCoordinate = nodeB.getX();
        int nodeBYCoordinate = nodeB.getY();

        int xDifference = nodeBXCoordinate - nodeAXCoordinate;
        int yDifference = nodeBYCoordinate - nodeAYCoordinate;

        return (int)Math.sqrt(xDifference*xDifference + yDifference*yDifference);
    }


}
