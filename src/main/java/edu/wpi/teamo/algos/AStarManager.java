package edu.wpi.teamo.algos;

import edu.wpi.teamo.map.database.*;
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

        //Convert Database types to Algorithm-friendly types
        for(NodeInfo rawNode : rawNodes) {
            Node n = new Node(rawNode.getNodeID(), rawNode.getXPos(), rawNode.getYPos(), rawNode.getFloor(), NodeType.valueOf(rawNode.getNodeType()), rawNode.getLongName(), rawNode.getShortName());
            allNodes.add(n);
        }
        for(EdgeInfo rawEdge : rawEdges) {
            Edge e = new Edge(rawEdge.getEdgeID(), rawEdge.getStartNodeID(), rawEdge.getEndNodeID());
            allEdges.add(e);
        }

        //Updating the adjacencies lists for nodes
        assignNodeAdjacency(allNodes,allEdges);

        aStar.setAllTheMess(allNodes);

    }

    /**
     * Helper function to update the adjacencies lists
     * @param nodes nodes to parse
     * @param edges edges to parse
     */
    public void assignNodeAdjacency(LinkedList<Node> nodes,LinkedList<Edge> edges){
        for(Node n: nodes){
            for (Edge e: edges){
                if (n.getID().equals(e.getStartNodeID())) n.addAdjacencyByNodeId(e.getEndNodeID());
                else if (n.getID().equals(e.getEndNodeID())) n.addAdjacencyByNodeId(e.getStartNodeID());
            }
        }
    }

    /**
     * TODO
     * @return LinkedList<Object></>
     */
    @Override
    public LinkedList<Object> getNodeIDLookUpTable() {
        return null;
    }

    /**
     * Returns the AStar Object
     * @return AStar object
     */
    @Override
    public AStar loadAStar() {
        return aStar;
    }

    /**
     * Returns the path between the starting node and ending node
     * @param startID ID of the starting node
     * @param endID ID of the ending node
     * @return The path between the starting and ending node
     */
    @Override
    public LinkedList<Node> getPath(String startID, String endID) {
        return aStar.findPath(startID,endID);
    }
}
