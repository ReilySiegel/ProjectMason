package edu.wpi.teamo.algos;

import edu.wpi.teamo.map.database.*;
import javafx.util.*;
import java.util.*;
import java.util.stream.*;

public class AStarManager implements AStarService {
    private MapDB DBService;
    private AStar aStar;

    public LinkedList<Node> allNodes;
    public LinkedList<Edge> allEdges;

    /**
     * Constructor for AStarManager
     * @param service database service for data retrieval
     */
    public AStarManager(MapDB service) {
        DBService = service;

        List<NodeInfo> rawNodes = DBService.getAllNodes().collect(Collectors.toList());
        List<EdgeInfo> rawEdges = DBService.getAllEdges().collect(Collectors.toList());

        allNodes = new LinkedList<>();
        allEdges = new LinkedList<>();

        //Lists for allocation
        //LinkedList<Node> nodes = new LinkedList<>();
        //Key = edgeID, Value = pair: (key = startID, value = endID)
        //LinkedList<Edge> edges = new LinkedList<>();

        //List<Pair<String, Pair<String,String>>> edges = new LinkedList<>();

        //Convert Database types to Algorithm-friendly types
        for(NodeInfo rawNode : rawNodes) {
            Node n = new Node(rawNode.getNodeID(), rawNode.getXPos(), rawNode.getYPos(), rawNode.getFloor(), NodeType.valueOf(rawNode.getNodeType()), rawNode.getLongName(), rawNode.getShortName());
            allNodes.add(n);
        }
        for(EdgeInfo rawEdge : rawEdges) {
            Edge e = new Edge(rawEdge.getEdgeID(), rawEdge.getStartNodeID(), rawEdge.getEndNodeID());
            allEdges.add(e);
        }

        // updating the adjacencies lists for nodes
        assignNodeAdjacency(allNodes,allEdges);

        aStar.setAllTheMess(allNodes);

    }

    /**
     * helper function to update the adjacencies lists
     * @param nodes
     * @param edges
     */
    public void assignNodeAdjacency(LinkedList<Node> nodes,LinkedList<Edge> edges){
        for(Node n: nodes){
            for (Edge e: edges){
                if (n.getID() == e.getStartNodeID()) n.addAdjacencyByNodeId(e.getEndNodeID());
                else if (n.getID() == e.getEndNodeID()) n.addAdjacencyByNodeId(e.getStartNodeID());
            }
        }
    }

    @Override
    public LinkedList<Object> getNodeIDLookUpTable() {
        return null;
    }

    /**
     * TODO: convert to void function?
     * @return AStar
     */
    @Override
    public AStar loadAStar() {
        return aStar;
    }

    @Override
    public LinkedList<Node> getPath(String startID, String endID) {
        return aStar.findPath(startID,endID);
    }
}
