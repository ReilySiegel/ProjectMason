package edu.wpi.teamo.views;

import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.NodeInfo;
import com.jfoenix.controls.JFXTextArea;
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

    ImageView imageView;
    AnchorPane nodePane;
    JFXTextArea mapText;

    Consumer<NodeInfo> onNodeClicked;
    Consumer<EdgeInfo> onEdgeClicked;

    static private boolean imagesLoaded = true;
    static final int imageWidth = 5000;
    static final int imageHeight = 3400;
    private double  lineThickness = 2.0;

    static Image L1FloorImage = null;
    static Image L2FloorImage = null;
    static Image groundFloorImage = null;
    static Image firstFloorImage = null;
    static Image secondFloorImage = null;
    static Image thirdFloorImage = null;

    public Map(ImageView imageView, AnchorPane nodePane, JFXTextArea mapText,
               Consumer<NodeInfo> onNodeClicked, Consumer<EdgeInfo> onEdgeClicked, Consumer<Pair<Integer, Integer>> onMapClicked) {
        this.imageView = imageView;
        this.nodePane = nodePane;
        this.mapText = mapText;

        this.onNodeClicked = onNodeClicked;
        this.onEdgeClicked = onEdgeClicked;

        nodePane.setOnMouseMoved((MouseEvent e) -> {
            int mapX = (int) transform((int) e.getX(), nodePane.getPrefWidth(), imageWidth);
            int mapY = (int) transform((int) e.getY(), nodePane.getPrefHeight(), imageHeight);
            mapText.setText("(" + mapX + " , " + mapY + ")");
        });

        if (onMapClicked != null) {
            nodePane.setOnMouseClicked((MouseEvent e) -> {
                int mapX = (int) transform((int) e.getX(), nodePane.getPrefWidth(), imageWidth);
                int mapY = (int) transform((int) e.getY(), nodePane.getPrefHeight(), imageHeight);
                onMapClicked.accept(new Pair<>(mapX, mapY));
            });
        }

    }

    public void drawPath(LinkedList<AlgoNode> path, String floor) {
        double firstX = 0;
        double firstY = 0;
        double secondX = 0;
        double secondY = 0;

        for(int i = 0;  i < (path.size() - 1); i++) {
            Color lineColor = path.get(i).getFloor().equals(floor) ? Color.RED : Color.GRAY;

            firstX = transform(path.get(i).getX(), imageWidth, nodePane.getPrefWidth());
            firstY = transform(path.get(i).getY(), imageHeight, nodePane.getPrefHeight());
            secondX = transform(path.get(i + 1).getX(), imageWidth, nodePane.getPrefWidth());
            secondY = transform(path.get(i + 1).getY(), imageHeight, nodePane.getPrefHeight());
            Line line = createLine(firstX, firstY, secondX, secondY, null, lineColor);
            nodePane.getChildren().add(line);
        }
    }

    public void drawFloorNodes(List<NodeInfo> allNodes, String floor) {
        /* filter only the nodes belonging to this floor */
        List<NodeInfo> floorNodes = allNodes.stream()
                .filter((NodeInfo node) -> node.getFloor().equals(floor))
                .collect(Collectors.toList());

        switchFloorImage(floor);

        nodePane.getChildren().removeAll(nodePane.getChildren());

        createCircles(floorNodes);
    }


    public void drawFloor(List<NodeInfo> allNodes, List<EdgeInfo> edges, String floor) {
        /* filter only the nodes belonging to this floor */
        List<NodeInfo> floorNodes = allNodes.stream()
                                            .filter((NodeInfo node) -> node.getFloor().equals(floor))
                                            .collect(Collectors.toList());

        switchFloorImage(floor);

        nodePane.getChildren().removeAll(nodePane.getChildren());

        createLines(edges, floorNodes);
        createCircles(floorNodes);
    }

    private void createLines(List<EdgeInfo> edges, List<NodeInfo> nodes) {

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
        line.setStrokeWidth(5);
        line.setStroke(lineColor);

        line.setOnMouseClicked(event -> {
            onEdgeClicked.accept(edge);
            event.consume();
        });
        line.setOnMouseEntered(event -> {
            line.setStroke(Color.GREEN);
            event.consume();
        });
        line.setOnMouseExited(event -> {
            line.setStroke(lineColor);
            event.consume();
        });

        return line;
    }

    private void createCircles(List<NodeInfo> nodes) {
        for (NodeInfo node : nodes) {
            double transformedX = transform(node.getXPos(), imageWidth,  nodePane.getPrefWidth());
            double transformedY = transform(node.getYPos(), imageHeight, nodePane.getPrefHeight());
            Circle circle = createCircle(transformedX, transformedY, onNodeClicked, node, mapText);
            nodePane.getChildren().add(circle);
        }
    }

    private static Circle createCircle(double x, double y, Consumer<NodeInfo> onNodeClicked, NodeInfo node, JFXTextArea mapText) {
        Circle circle = new Circle(x, y, 4, Color.BLUE);
        circle.setOnMouseEntered(event -> {
            mapText.setText(node.getNodeID() + "\t" + node.getLongName());
            circle.setRadius(7);
            event.consume();
        });
        circle.setOnMouseExited(event -> {
            if (mapText != null) {
                mapText.setText("");
            }
            circle.setRadius(4);
            event.consume();
        });
        circle.setOnMousePressed(event -> {
            onNodeClicked.accept(node);
            event.consume();
        });
        return circle;
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

    private static double transform(int val, double from, double to) {
        return val * (to / from);
    }

    public double getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(double lineThickness) {
        this.lineThickness = lineThickness;
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
                default:
                    imageView.setImage(thirdFloorImage);
                    break;
            }
        }
        else {
            System.out.println("ERROR: IMAGES NOT LOADED, CANNOT SWITCH FLOOR IMAGE");
        }
    }

    static public void loadImages() {
        L1FloorImage = new Image("/edu/wpi/teamo/images/00_thelowerlevel1.png");

        L2FloorImage = new Image("/edu/wpi/teamo/images/00_thelowerlevel2.png");

        groundFloorImage = new Image("/edu/wpi/teamo/images/00_thegroundfloor.png");

        firstFloorImage = new Image("/edu/wpi/teamo/images/01_thefirstfloor.png");

        secondFloorImage = new Image("/edu/wpi/teamo/images/02_thesecondfloor.png");

        thirdFloorImage = new Image("/edu/wpi/teamo/images/03_thethirdfloor.png");
    }
}
