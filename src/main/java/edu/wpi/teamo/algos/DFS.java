package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.Edge;
import javafx.util.Pair;

import java.util.*;

public class DFS {
    private LinkedList<AlgoNode> allNodes;
    private String startID;
    private String endID;
    private boolean[] marked;
    private String[] edgeTo;
    private LinkedList<String>[] adj; // adjacency lists
    private Hashtable<String, Integer> position;

    private AlgoNode sourceNode;


    public DFS(LinkedList<AlgoNode> nodes){
        this.allNodes = nodes;
        this.marked = new boolean[nodes.size()];
        this.edgeTo = new String[nodes.size()];
        this.adj = (LinkedList<String>[]) new  LinkedList[nodes.size()];
        this.position = new Hashtable<>();
        int i = 0;
        for (AlgoNode n: nodes){
            adj[i] = n.getAdjacencies();
            position.put(n.getID(),i);
            i++;
        }
        this.sourceNode = nodes.getFirst();

        dfs(sourceNode.getID());
    }


    public DFS(LinkedList<AlgoNode> nodes, String startNodeID, String endNodeID){
        this.startID = startNodeID;
        this.endID = endNodeID;
        this.allNodes = nodes;
        this.marked = new boolean[nodes.size()];
        this.edgeTo = new String[nodes.size()];
        this.adj = (LinkedList<String>[]) new  LinkedList[nodes.size()];
        this.position = new Hashtable<>();

        int i = 0;
        for (AlgoNode n: nodes){
            adj[i] = n.getAdjacencies();
            position.put(n.getID(),i);
            i++;

        }

        dfs(startNodeID);
    }



    public LinkedList<AlgoNode> seekForIsolatedNodes(){

        LinkedList<AlgoNode> allIsolatedNodes = new LinkedList<>();

        for (int i = 0; i < allNodes.size(); i++) {
            if (!marked[i]){
                AlgoNode isolatedNode = allNodes.get(i);
                allIsolatedNodes.add(isolatedNode);
            }
        }

        return allIsolatedNodes;

    }

    public LinkedList<AlgoNode> findPath() throws NullPointerException{
        try{
            LinkedList<AlgoNode> path_Node = new LinkedList<>();
            Iterable<String> path_String = pathTo(endID);
            for (String s: path_String){
                path_Node.addFirst(stringToNode(s));
            }

            return path_Node;}
        catch (Exception e){
            System.out.println("There is no path between " +startID+ " and " +endID);
            return null;
        }
    }

    private void dfs(String targetNode){

        int indexOfTargetID = position.get(targetNode);

        marked[indexOfTargetID]  = true;


        for (String w: adj[indexOfTargetID]){
            if (!marked[position.get(w)]){
                edgeTo[position.get(w)] = targetNode;
                dfs(w);
            }
        }
    }

    private boolean hasPathTo(String targetNode){
        return marked[position.get(targetNode)];
    }

    private Iterable<String> pathTo(String targetNode){

        if (!hasPathTo(targetNode)) {System.out.println("No path found!"); return null;}
        Stack<String> path = new Stack<>();
        for(String x = targetNode; x != startID; x = edgeTo[position.get(x)])
            path.push(x);
        path.push(startID);
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

    public void setNodes(LinkedList<AlgoNode> nodes) {
        this.allNodes = nodes;
    }
}
