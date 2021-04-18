package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.*;

public class        AStarManager implements AStarService {
    private IMapService DBService;

    /**
     * Constructor for AStarManager
     * @param service database service for data retrieval
     */
    public AStarManager(IMapService service) {
        DBService = service;
    }

    /**
     * Helper function to update the adjacencies lists
     * @param nodes nodes to parse
     * @param edges edges to parse
     */
    public static void assignNodeAdjacency(LinkedList<AlgoNode> nodes, LinkedList<Edge> edges){
        for(AlgoNode n: nodes){
            for (Edge e: edges){
                if (n.getID().equals(e.getStartNodeID())) n.addAdjacencyByNodeId(e.getEndNodeID());
                else if (n.getID().equals(e.getEndNodeID())) n.addAdjacencyByNodeId(e.getStartNodeID());
            }
        }
    }

    /**
     * Returns the path between the starting node and ending node
     * @param startID ID of the starting node
     * @param endID ID of the ending node
     * @return The path between the starting and ending node
     */
    @Override
    public LinkedList<AlgoNode> getPath(String startID, String endID) throws SQLException {

        List<NodeInfo> rawNodes = DBService.getAllNodes().collect(Collectors.toList());
        List<EdgeInfo> rawEdges = DBService.getAllEdges().collect(Collectors.toList());

        //Convert Database types to Algorithm-friendly types
        LinkedList<AlgoNode> allNodes = new LinkedList<>();
        for(NodeInfo rawNode : rawNodes) {
            AlgoNode n = new AlgoNode(rawNode.getNodeID(), rawNode.getXPos(), rawNode.getYPos(), rawNode.getFloor(), NodeType.valueOf(rawNode.getNodeType()), rawNode.getLongName(), rawNode.getShortName());
            allNodes.add(n);
        }

        LinkedList<Edge> allEdges = new LinkedList<>();
        for(EdgeInfo rawEdge : rawEdges) {
            Edge e = new Edge(rawEdge.getEdgeID(), rawEdge.getStartNodeID(), rawEdge.getEndNodeID());
            allEdges.add(e);
        }

        assignNodeAdjacency(allNodes,allEdges);

        AStar aStar = new AStar(allNodes, allEdges, startID, endID);
        aStar.setAllTheNodes(allNodes);
        return aStar.findPath(startID,endID);
    }
}
