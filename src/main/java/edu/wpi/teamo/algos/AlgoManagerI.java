package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.Edge;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.IMapService;
import edu.wpi.teamo.database.map.NodeInfo;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An abstract class
 */
public abstract class AlgoManagerI implements IStrategyPathfinding {
    protected IMapService DBService;
    protected LinkedList<AlgoNode> nodes;
    protected List<EdgeInfo> edges;
    public AlgoManagerI(IMapService service) {
        this.DBService = service;
    }

    /**
     * Helper function to update the adjacencies lists
     * @param nodes nodes to parse
     * @param edges edges to parse
     */
    protected static void assignNodeAdjacency(LinkedList<AlgoNode> nodes, List<EdgeInfo> edges){
        for(AlgoNode n: nodes){
            for (EdgeInfo e: edges){
                if (n.getID().equals(e.getStartNodeID())) n.addAdjacencyByNodeId(e.getEndNodeID());
                else if (n.getID().equals(e.getEndNodeID())) n.addAdjacencyByNodeId(e.getStartNodeID());
            }
        }
    }

    /**
     * Convert Database types to Algorithm-friendly types and update the fields
     * @throws SQLException
     */
    protected void conditionSetup() throws SQLException {
        List<NodeInfo> rawNodes = DBService.getAllNodes().collect(Collectors.toList());
        List<EdgeInfo> rawEdges = DBService.getAllEdges().collect(Collectors.toList());

        //Convert Database types to Algorithm-friendly types
        LinkedList<AlgoNode> allNodes = new LinkedList<>();
        for(NodeInfo rawNode : rawNodes) {
            AlgoNode n;
            try {
                n = new AlgoNode(rawNode.getNodeID(), rawNode.getXPos(), rawNode.getYPos(), rawNode.getFloor(), NodeType.valueOf(rawNode.getNodeType()), rawNode.getLongName(), rawNode.getShortName());
            }
            catch(IllegalArgumentException e) {
                n = new AlgoNode(rawNode.getNodeID(), rawNode.getXPos(), rawNode.getYPos(), rawNode.getFloor(), NodeType.NULL, rawNode.getLongName(), rawNode.getShortName());
            }
            allNodes.add(n);
        }

        assignNodeAdjacency(allNodes,rawEdges);

        this.nodes = allNodes;
        this.edges = rawEdges;

    }

    /**
     * Returns the path between the starting node and ending node
     * @param startID ID of the starting node
     * @param endID ID of the ending node
     * @return The path between the starting and ending node
     */
    @Override
    public LinkedList<AlgoNode> getPath(String startID, String endID) throws SQLException {
        return null;
    }
}


