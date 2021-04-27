package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.Edge;

import java.util.*;

public class AStar {

    private String startID;
    private String endID;
    private LinkedList<AlgoNode> allTheNodes;



    public AStar(LinkedList<AlgoNode> nodes, String startNodeID, String endNodeID) {
        this.startID = startNodeID;
        this.endID = endNodeID;
        this.allTheNodes = nodes;

    }



    public void setAllTheNodes(LinkedList<AlgoNode> all){
        allTheNodes = all;
    }


    /**
     * The ultimate A* pathfinding function that prints out the list in the order of Start --- End
     * @param startingNode (starting position)
     * @param endNode (destination)
     * @return a LinkedList of the path between the starting and ending nodes
     */
    public LinkedList<AlgoNode> findPath(String startingNode, String endNode) {
        startID = startingNode;
        endID = endNode;
        findPathPreGame();
        LinkedList<AlgoNode> path = new LinkedList<AlgoNode>();
        path = generatePath();
        return path;
    }


    /**
     * The core of A*, setting all the gCost, hCost and direction of the path
     */
    private void findPathPreGame() {
        AlgoNode starting = stringToNode(startID);
        AlgoNode ending = stringToNode(endID);

        // list of nodes that are open to be evaluated
        LinkedList<AlgoNode> openNodes = new LinkedList<>();

        // Hashset of nodes has already been visited and evaluated
        HashSet<AlgoNode> visitedNodes = new HashSet<>();

        // adding the starting node to the openNodes list
        openNodes.add(starting);

        //HashMap<String, AlgoNode> Nodes = allNodes;

        // looping
        while(!openNodes.isEmpty()){

            AlgoNode currentNode = openNodes.getFirst();

            // traverse through the open list and find the node with the lowest fCost
            for (AlgoNode n: openNodes){
                if (lowerFcost(n,currentNode)){
                    currentNode = n;
                }
            }
            //remove the current node from the openNodes list and add it to the visited nodes list
            openNodes.remove(currentNode);
            visitedNodes.add(currentNode);

            // if true, path has been found, then return
            if (isEnd(currentNode)){return; }

            for (AlgoNode neighbor: adjacenciesToNodes(currentNode) ){ //adjacencies has to list of  nodes

                // we need to discuss about the conditions that make a node "walkable" - all our nodes are walkable
                //skip to the next neighbor
                if (visitedNodes.contains(neighbor))
                    continue;

                int newMovementCostToNeighbor = currentNode.get_gCost() + getDistance(currentNode,neighbor  );

                if (newMovementCostToNeighbor < neighbor.get_gCost() || !openNodes.contains(neighbor)){
                    neighbor.set_gCost(newMovementCostToNeighbor);
                    neighbor.set_hCost(getDistance(neighbor,ending));
                    neighbor.setParent(currentNode);

                    if (!openNodes.contains(neighbor)) openNodes.add(neighbor);
                }
            }
        }
    }

    /**
     * returns true if the end node is the same as the current id
     * @param n node to test
     * @return true of end node, false otherwise
     */
    public boolean isEnd(AlgoNode n)
    {
        return n == stringToNode(endID)|| n.getID().equals(endID);
    }

    /**
     * checks between the two nodes passed which one has the lower f cost and
     * if the fcost is equal it prioritizes
     * TODO: (Made public for now since it was throwing errors in the tests)
     * @param n node to check
     * @param currentNode current node
     * @return true if n's fcost is lower than currentNode's fcost, or if the fcosts are the same and if n's hcost is lower than currentNode's hcost, and false otherwise
     */
    public boolean lowerFcost(AlgoNode n , AlgoNode currentNode)

    {
        return n.get_fCost() < currentNode.get_fCost() ||
                (n.get_fCost() == currentNode.get_fCost() && n.get_hCost() < currentNode.get_hCost());
    }
    /**
     * Returns the specified node's adjacent nodes
     * @param node the node from which to retrieve the adjacent nodes
     * @return The list of adjacent nodes
     */
    public LinkedList<AlgoNode> adjacenciesToNodes(AlgoNode node) {
        LinkedList<AlgoNode> list = new LinkedList<>();
        for (String s : node.getAdjacencies()) {
            for (AlgoNode n : allTheNodes) {
                if (n.getID().equals(s)) list.add(n);
            }
        }
        return list;
    }

    /**
     * Returns the AlgoNode associated with the ID
     * @param ID the ID of the node to retrieve
     * @return the AlgoNode associated with the ID
     */
    private AlgoNode stringToNode(String ID){
        try {
            for (AlgoNode n : allTheNodes) {
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
