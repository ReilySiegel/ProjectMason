package edu.wpi.teamo.views;

import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.NodeInfo;
import com.jfoenix.controls.JFXTextArea;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import java.io.FileNotFoundException;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import edu.wpi.teamo.algos.AlgoNode;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.io.FileInputStream;
import java.util.LinkedList;
import javafx.util.Pair;
import java.util.List;

public class Map  {

    private Consumer<Pair<Circle, NodeInfo>> onDrawNode = null;
    private Consumer<Pair<Line, EdgeInfo>> onDrawEdge = null;

    static private boolean imagesLoaded = false;
    static public final int imageHeight = 3400;
    static public final int imageWidth = 5000;

    static Image groundFloorImage = null;
    static Image secondFloorImage = null;
    static Image firstFloorImage = null;
    static Image thirdFloorImage = null;
    static Image L1FloorImage = null;
    static Image L2FloorImage = null;

    private final AnchorPane nodePane;
    private final ImageView imageView;

    public Map(ImageView imageView, AnchorPane nodePane) {
        this.imageView = imageView;
        this.nodePane = nodePane;
    }

    public void drawPath(LinkedList<AlgoNode> path, String floor) {
        for(int i = 0;  i < (path.size() - 1); i++) {
            Color lineColor = path.get(i).getFloor().equals(floor) ? Color.RED : Color.GRAY;

            double firstX = transform(path.get(i).getX(), imageWidth, nodePane.getPrefWidth());
            double firstY = transform(path.get(i).getY(), imageHeight, nodePane.getPrefHeight());
            double secondX = transform(path.get(i + 1).getX(), imageWidth, nodePane.getPrefWidth());
            double secondY = transform(path.get(i + 1).getY(), imageHeight, nodePane.getPrefHeight());

            Line line = createLine(firstX, firstY, secondX, secondY, null, lineColor);
            nodePane.getChildren().add(line);
        }
    }

    public void drawNodes(List<NodeInfo> allNodes, String floor) {
        List<NodeInfo> floorNodes = filterFloor(allNodes, floor);

        switchFloorImage(floor);

        addCircles(floorNodes);
    }


    public void drawEdges(List<NodeInfo> allNodes, List<EdgeInfo> edges, String floor) {
        List<NodeInfo> floorNodes = filterFloor(allNodes, floor);

        switchFloorImage(floor);

        addLines(edges, floorNodes);
    }

    public void clearShapes() {
        nodePane.getChildren().removeAll(nodePane.getChildren());
    }

    private void addLines(List<EdgeInfo> edges, List<NodeInfo> nodes) {

        for (EdgeInfo edge : edges) {
            NodeInfo startNode = findNode(edge.getStartNodeID(), nodes);
            NodeInfo endNode = findNode(edge.getEndNodeID(), nodes);

            if (startNode != null && endNode != null) {
                double tfStartX = transform(startNode.getXPos(), imageWidth, nodePane.getPrefWidth());
                double tfStartY = transform(startNode.getYPos(), imageHeight, nodePane.getPrefHeight());
                double tfEndX = transform(endNode.getXPos(), imageWidth, nodePane.getPrefWidth());
                double tfEndY = transform(endNode.getYPos(), imageHeight, nodePane.getPrefHeight());
                Line line = createLine(tfStartX, tfStartY, tfEndX, tfEndY, edge, Color.RED);
                nodePane.getChildren().add(line);
            }
        }

    }

    private Line createLine(double StartX, double StartY, double EndX, double EndY, EdgeInfo edge, Color lineColor) {
        Line line = new Line(StartX, StartY, EndX, EndY);
        line.setStroke(lineColor);
        line.setStrokeWidth(5);

        if (onDrawEdge != null) {
            onDrawEdge.accept(new Pair<>(line, edge));
        }

        return line;
    }

    private void addCircles(List<NodeInfo> nodes) {
        for (NodeInfo node : nodes) {
            double transformedX = transform(node.getXPos(), imageWidth,  nodePane.getPrefWidth());
            double transformedY = transform(node.getYPos(), imageHeight, nodePane.getPrefHeight());
            Circle circle = createCircle(transformedX, transformedY, node);
            nodePane.getChildren().add(circle);
        }
    }

    private Circle createCircle(double x, double y, NodeInfo node) {
        Circle circle = new Circle(x, y, 4, Color.BLUE);

        if (onDrawNode != null) {
            onDrawNode.accept(new Pair<>(circle, node));
        }

        return circle;
    }

    /**
     *  filter only the nodes belonging to this floor
     **/
    private static List<NodeInfo> filterFloor(List<NodeInfo> nodes, String floor) {
        return nodes.stream()
               .filter((NodeInfo node) -> node.getFloor().equals(floor))
               .collect(Collectors.toList());
    }

    private static NodeInfo findNode(String id, List<NodeInfo> nodes) {
        NodeInfo foundNode = null;

        for (NodeInfo node : nodes) {
            if (node.getNodeID().equals(id)) {
                foundNode = node;
            }
        }

        return foundNode;
    }

    public static double transform(int val, double from, double to) {
        return val * (to / from);
    }

    public void setOnMouseMoved(Consumer<Pair<Integer, Integer>> onMouseMoved) {
        nodePane.setOnMouseMoved((MouseEvent e) -> {
            /* transform anchorPane coords to the map pixel coords */
            int mapX = (int) transform((int) e.getX(), nodePane.getPrefWidth(), imageWidth);
            int mapY = (int) transform((int) e.getY(), nodePane.getPrefHeight(), imageHeight);
            onMouseMoved.accept(new Pair<>(mapX, mapY));
        });
    }

    public void setOnMapClicked(Consumer<Pair<Integer, Integer>> onMapClicked) {
        nodePane.setOnMouseClicked((MouseEvent e) -> {
            /* transform anchorPane coords to the map pixel coords */
            int mapX = (int) transform((int) e.getX(), nodePane.getPrefWidth(), imageWidth);
            int mapY = (int) transform((int) e.getY(), nodePane.getPrefHeight(), imageHeight);
            onMapClicked.accept(new Pair<>(mapX, mapY));
        });
    }

    public void setOnDrawNode(Consumer<Pair<Circle, NodeInfo>> onDrawNode) {
        this.onDrawNode = onDrawNode;
    }

    public void setOnDrawEdge(Consumer<Pair<Line, EdgeInfo>> onDrawEdge) {
        this.onDrawEdge = onDrawEdge;
    }

    public void switchFloorImage(String floor){
        if (imagesLoaded) {
            switch (floor) {
                case "L1":
                    imageView.setImage(L1FloorImage);
                    break;
                case "L2":
                    imageView.setImage(L2FloorImage);
                    break;
                case "G":
                    imageView.setImage(groundFloorImage);
                    break;
                case "1":
                    imageView.setImage(firstFloorImage);
                    break;
                case "2":
                    imageView.setImage(secondFloorImage);
                    break;
                case "3":
                    imageView.setImage(thirdFloorImage);
                    break;
            }
        }
        else {
            System.out.println("ERROR: IMAGES NOT LOADED, CANNOT SWITCH FLOOR IMAGE");
        }
    }

    static public void loadImages() {
        try {

            L2FloorImage = new Image("/edu/wpi/teamo/images/00_thelowerlevel2.png");

            L1FloorImage = new Image("/edu/wpi/teamo/images/00_thelowerlevel1.png");

            groundFloorImage = new Image("/edu/wpi/teamo/images/00_thegroundfloor.png");

            firstFloorImage = new Image("/edu/wpi/teamo/images/01_thefirstfloor.png");

            secondFloorImage = new Image("/edu/wpi/teamo/images/02_thesecondfloor.png");

            thirdFloorImage = new Image("/edu/wpi/teamo/images/03_thethirdfloor.png");

            imagesLoaded = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
