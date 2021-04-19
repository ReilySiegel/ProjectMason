package edu.wpi.teamo.views;

import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Map {

    Canvas canvas;
    GraphicsContext gc;

    static final int imageWidth = 5000;
    static final int imageHeight = 3400;

    public Map(Canvas canvas) {
        this.gc = canvas.getGraphicsContext2D();
        this.canvas = canvas;
    }

    public void drawMap(List<NodeInfo> nodes, List<EdgeInfo> edges) {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);

        for (NodeInfo node : nodes) {
            double transformedX = transformX(node.getXPos());
            double transformedY = transformY(node.getYPos());

            gc.fillOval(transformedX, transformedY, 5, 5);
        }

        for (EdgeInfo edge : edges) {
            NodeInfo startNode = findNode(edge.getStartNodeID(), nodes);
            NodeInfo endNode = findNode(edge.getEndNodeID(), nodes);

            if (startNode != null && endNode != null) {
                double tfStartX = transformX(startNode.getXPos());
                double tfStartY = transformY(startNode.getYPos());
                double tfEndX = transformX(endNode.getXPos());
                double tfEndY = transformY(endNode.getYPos());
                gc.strokeLine(tfStartX, tfStartY, tfEndX, tfEndY);
            }

        }

    }

    private NodeInfo findNode(String id, List<NodeInfo> nodes) {
        NodeInfo foundNode = null;

        for (NodeInfo node : nodes) {
            if (node.getNodeID().equals(id)) {
                foundNode = node;
            }
        }

        return foundNode;
    }

    private double transformX(int nodeX) {
        return nodeX * (canvas.getWidth() / imageWidth);
    }

    private double transformY(int nodeY) {
        return nodeY * (canvas.getHeight() / imageHeight);
    }

}
