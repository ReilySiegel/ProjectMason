package edu.wpi.teamo.algos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


public class AStar {
    // still not quite understand how the MapSection works so I made a hashmap instead for now
    // considering using array tho for MapSection
    // private LinkedList<MapSection> maps;
    private String startNodeID;
    private String endNodeID;
    private HashMap<String, Node> allNodes;


    // WE MIGHT NEED A CONSTRUCTOR????
    public AStar(HashMap<String, Node> nodes){
        allNodes = nodes;
    }


    /**
     * The ultimate A* pathfinding function that prints out the list in the order of Start --- End
     * @param startingNode (starting position)
     * @param endNode (destination)
     * @return a linkedlist
     */
    private LinkedList<Node> findPath(Node startingNode, Node endNode){

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
    private void findPathPreGame(Node start, Node end){

        // list of nodes that are open to be evaluated
        LinkedList<Node> openNodes = new LinkedList<>();

        // Hashset of nodes has already been visited and evaluated
        HashSet<Node> visitedNodes = new HashSet<>();

        // adding the starting node to the openNodes list
        openNodes.add(start);

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
            if (currentNode == end){return; }


            for (Node s: currentNode.getAdjacencies()){ //adjacencies has to list of  nodes
                Node neighbor = openNodes.get(openNodes.indexOf(s));
                // we need to discuss about the conditions that make a node "walkable" - all our nodes are walkable
                //skip to the next neighbor
                if (visitedNodes.contains(neighbor))
                    continue;

                int newMovementCostToNeighbor = currentNode.get_gCost() + getDistance(currentNode,neighbor  );

                if (newMovementCostToNeighbor < neighbor.get_gCost() || !openNodes.contains(neighbor)){
                    neighbor.set_gCost(newMovementCostToNeighbor);
                    neighbor.set_hCost(getDistance(neighbor,end));
                    neighbor.setParent(currentNode);

                    if (!openNodes.contains(neighbor)) openNodes.add(neighbor);
                }
            }
        }
    }

    /**
     * A temporary silly helper function waiting to be placed with conditions that determine if a node is "walkable"
     * @param a (you need to admit that Oliver is cool by entering "absolutely" to have the Pathfinding method working!!)
     * @return a boolean value
     */
    private Boolean isOliverCool (String a){
        return a.equals("absolutely");
    }


    /**
     * creates a path based using the list of nodes
     * @param startNode
     * @param endNode
     * @return a linked list of path from startNode ---> endNode
     */
    private LinkedList<Node> generatePath(Node startNode, Node endNode){

        LinkedList<Node> path = new LinkedList<Node>();
        Node currentNode = endNode;

        while(currentNode != startNode){
            path.addFirst(currentNode);
            currentNode = currentNode.getParent();
        }
        return path;
    }

    /**
     * Get distance between two nodes using distance formula
     * @param nodeA (starting Node)
     * @param nodeB (target Node)
     * @return the distance between two Nodes
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
