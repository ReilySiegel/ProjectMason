package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.Edge;

import java.util.LinkedList;

import java.util.Queue;

public class BFS {

    private LinkedList<AlgoNode> nodes;
    private String startID;
    private String endID;

    public BFS(LinkedList<AlgoNode> nodes, String startNodeID, String endNodeID) {
        this.startID = startNodeID;
        this.endID = endNodeID;
        this.nodes = nodes;
    }


    public LinkedList<AlgoNode> findPath(String startId, String endId) {
        LinkedList<AlgoNode> path = new LinkedList<>();

        //perform bfs and assign parents to each node in the list of nodes
        bfsSolve(nodes, startId);

        //construct the path based on the asssigned parents
        path = reconstructPath();

        return path;

    }


    public void bfsSolve(LinkedList<AlgoNode> nodes, String startID) {
        AlgoNode startNode = idToNode(startID);

        Queue<AlgoNode> queue = new LinkedList<>();

        boolean visited[] = new boolean[nodes.size()];

        queue.add(startNode);
        visited[nodes.indexOf(startNode)] = true;


        while(queue.size() != 0){
            AlgoNode currNode = queue.poll();
            LinkedList<AlgoNode> neighbors = getNeighbors(currNode);

            for(AlgoNode  n: neighbors){
                try {
                    if (!visited[nodes.indexOf(n)]) {
                        queue.add(n);
                        visited[nodes.indexOf(n)] = true;
                        n.setParent(currNode);
                    }
                }
                catch(ArrayIndexOutOfBoundsException e){
                    System.out.println("Out of bounds exception was here");
                }
            }

        }
    }

    public LinkedList<AlgoNode> reconstructPath() {
        AlgoNode start = idToNode(startID);
        AlgoNode end = idToNode(endID);

        LinkedList<AlgoNode> path = new LinkedList<AlgoNode>();
        AlgoNode currentNode = end;

        while(currentNode != start){
            path.addFirst(currentNode);
            currentNode = currentNode.getParent();
        }
        path.addFirst(start);
        return path;
    }

    public AlgoNode idToNode(String nodeID) {
         for(AlgoNode node: nodes) {
           if( node.getID().equals(nodeID)){
               return node;
         }
    }
        System.out.println("Node not in the graph");
        return null;
    }


    public LinkedList<AlgoNode> getNeighbors(AlgoNode node) {
        LinkedList<AlgoNode> neighbors = new LinkedList<>();
        for (String nodeID : node.getAdjacencies()) {
            neighbors.add(idToNode(nodeID));

        }

        return neighbors;

    }

    public void setNodes(LinkedList<AlgoNode> nodes) {
        this.nodes = nodes;
    }


}

