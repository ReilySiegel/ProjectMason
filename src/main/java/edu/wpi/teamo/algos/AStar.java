package edu.wpi.teamo.algos;

import java.util.*;

public class AStar {

    private LinkedList<Node> allTheMess;

    public void setAllTheMess(LinkedList<Node> all){
        allTheMess = all;
    }


    /**
     * The ultimate A* pathfinding function that prints out the list in the order of Start --- End
     * @param startingNode (starting position)
     * @param endNode (destination)
     * @return a LinkedList of the path between the starting and ending nodes
     */
    public LinkedList<Node> findPath(String startingNode, String endNode){


        findPathPreGame(startingNode, endNode);
        LinkedList<Node> path = new LinkedList<Node>();
        path = generatePath(startingNode, endNode);
        return path;
    }


    /**
     * The core of A*, setting all the gCost, hCost and direction of the path
     * @param start (starting position)
     * @param end (destination)
     */
    private void findPathPreGame(String start, String end) {
        Node starting = stringToNode(start);
        Node ending = stringToNode(end);

        // list of nodes that are open to be evaluated
        LinkedList<Node> openNodes = new LinkedList<>();

        // Hashset of nodes has already been visited and evaluated
        HashSet<Node> visitedNodes = new HashSet<>();

        // adding the starting node to the openNodes list
        openNodes.add(starting);

        //HashMap<String, Node> Nodes = allNodes;

        // looping
        while(!openNodes.isEmpty()){

            Node currentNode = openNodes.getFirst();

            // traverse through the open list and find the node with the lowest fCost
            for (Node n: openNodes){
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

            for (Node neighbor: adjacenciesToNodes(currentNode) ){ //adjacencies has to list of  nodes

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
    private LinkedList<Node> adjacenciesToNodes(Node node) {
        LinkedList<Node> list = new LinkedList<>();
        for (String s : node.getAdjacencies()) {
            for (Node n : allTheMess) {
                if (n.getID().equals(s)) list.add(n);
            }
        }
        return list;
    }

    /**
     * Returns the Node associated with the ID
     * @param ID the ID of the node to retrieve
     * @return the Node associated with the ID
     */
    private Node stringToNode(String ID){
        for (Node n: allTheMess) {
            if (ID.equals(n.getID())) {
                return n;
            }
        }
        return null;
    }

    /**
     * Creates a path based using the list of nodes
     * @param startNode The starting node
     * @param endNode The end node
     * @return a linked list of path from startNode ---> endNode
     */
    private LinkedList<Node> generatePath(String startNode, String endNode){
        Node start = stringToNode(startNode);
        Node end = stringToNode(endNode);

        LinkedList<Node> path = new LinkedList<Node>();
        Node currentNode = end;

        while(currentNode != start){
            path.addFirst(currentNode);
            currentNode = currentNode.getParent();
        }

        path.addFirst(start);
        return path;
    }

    /**
     * Get distance between two nodes using distance formula
     * @param nodeA (starting Node)
     * @param nodeB (target Node)
     * @return The distance between the two nodes
     */
    private int getDistance(Node nodeA, Node nodeB){
        int nodeAXCoordinate = nodeA.getX();
        int nodeAYCoordinate = nodeA.getY();
        int nodeBXCoordinate = nodeB.getX();
        int nodeBYCoordinate = nodeB.getY();

        int xDifference = nodeBXCoordinate - nodeAXCoordinate;
        int yDifference = nodeBYCoordinate - nodeAYCoordinate;

        return (int)Math.sqrt(xDifference*xDifference + yDifference*yDifference);
    }

}
