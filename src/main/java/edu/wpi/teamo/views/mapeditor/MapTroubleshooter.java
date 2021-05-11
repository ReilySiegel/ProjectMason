package edu.wpi.teamo.views.mapeditor;

import edu.wpi.teamo.App;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.algos.DFSManager;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.views.Map;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MapTroubleshooter {

    public static void troubleshootNodes(Stream<NodeInfo> nodes) {

        List<String> isolatedNodeIDs = findIsolatedNodeIDs().collect(Collectors.toList());

        nodes.forEach(node -> {
            boolean valid = true;

            valid &= !nodeOutOfBounds(node);
            valid &= !invalidFloor(node);
            valid &= !isolatedNodeIDs.contains(node.getNodeID());

            if (valid != node.isValid()) {
                setNodeValidity(node.getNodeID(), valid);
            }
        });

    }

    private static boolean nodeOutOfBounds(NodeInfo node) {
        return !edu.wpi.teamo.views.Map.isWithinMapBounds(node.getXPos(), node.getYPos());
    }

    private static boolean invalidFloor(NodeInfo node) {
        List<String> validFloors = Arrays.asList(Map.floorKeys);
        return !validFloors.contains(node.getFloor());
    }

    private static Stream<String> findIsolatedNodeIDs() {
        try {
            return (new DFSManager(App.mapService)).getIsolatedNodes().stream().map(AlgoNode::getID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return Stream.empty();
        }
    }

    private static void setNodeValidity(String id, boolean valid) {
        try {
            App.mapService.setNodeValid(id, valid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void troubleshootEdges(Stream<EdgeInfo> edges, Stream<NodeInfo> nodes) {
        List<String> nodeIDs = nodes.map(NodeInfo::getNodeID).collect(Collectors.toList());

        edges.forEach(edge -> {
            boolean valid = true;

            valid &= !edge.getStartNodeID().equals(edge.getEndNodeID());
            valid &= nodeIDs.contains(edge.getStartNodeID());
            valid &= nodeIDs.contains(edge.getEndNodeID());

            if (valid != edge.isValid()) {
                setEdgeValidity(edge.getEdgeID(), valid);
            }
        });

    }

    private static void setEdgeValidity(String id, boolean valid) {
        try {
            App.mapService.setEdgeValid(id, valid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
