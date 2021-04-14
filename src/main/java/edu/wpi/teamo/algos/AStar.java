package edu.wpi.teamo.algos;

import edu.wpi.teamo.map.database.Edge;

import java.util.*;

public class AStar {

    LinkedList<AlgoNode> nodes;
    LinkedList<Edge> edges;
    String startID;
    String endID;

    public AStar(LinkedList<AlgoNode> nodes, LinkedList<Edge> edges, String startNodeID, String endNodeID) {
        this.startID = startNodeID;
        this.endID = endNodeID;
        this.nodes = nodes;
        this.edges = edges;
    }

    private LinkedList<AlgoNode> allTheMess;

    public void setAllTheMess(LinkedList<AlgoNode> all){
        allTheMess = all;
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
                if (n.get_fCost() < currentNode.get_fCost() ||
                        (n.get_fCost() == currentNode.get_fCost() && n.get_hCost() < currentNode.get_hCost())){
                    currentNode = n;
                }
            }
            //remove the current node from the openNodes list and add it to the visited nodes list
            openNodes.remove(currentNode);
            visitedNodes.add(currentNode);

            // if ture, path has been found, then return
            if (currentNode == ending){return; }

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
     * Returns the specified node's adjacent nodes
     * @param node the node from which to retrieve the adjacent nodes
     * @return The list of adjacent nodes
     */
    private LinkedList<AlgoNode> adjacenciesToNodes(AlgoNode node) {
        LinkedList<AlgoNode> list = new LinkedList<>();
        for (String s : node.getAdjacencies()) {
            for (AlgoNode n : allTheMess) {
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
        for (AlgoNode n: allTheMess) {
            if (ID.equals(n.getID())) {
                return n;
            }
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
