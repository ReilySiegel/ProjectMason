package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXTextArea;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.scene.layout.AnchorPane;
import java.io.FileNotFoundException;

import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.io.FileInputStream;
import java.util.List;

public class Map  {

    ImageView imageView;
    AnchorPane nodePane;
    JFXTextArea mapText;

    Consumer<NodeInfo> onNodeClicked;
    Consumer<EdgeInfo> onEdgeClicked;

    static final int imageWidth = 5000;
    static final int imageHeight = 3400;
    static private boolean imagesLoaded = true;
    private double  lineThickness = 2.0;
    private Color lineColor = Color.RED;

    static Image L1FloorImage = null;
    static Image L2FloorImage = null;
    static Image groundFloorImage = null;
    static Image firstFloorImage = null;
    static Image secondFloorImage = null;
    static Image thirdFloorImage = null;

    public Map(ImageView imageView, AnchorPane nodePane, JFXTextArea mapText,
               Consumer<NodeInfo> onNodeClicked, Consumer<EdgeInfo> onEdgeClicked) {
        this.imageView = imageView;
        this.nodePane = nodePane;
        this.mapText = mapText;

        this.onNodeClicked = onNodeClicked;
        this.onEdgeClicked = onEdgeClicked;

    }

    public void drawPath(LinkedList<AlgoNode> path, String floor) {
        double firstX = 0;
        double firstY = 0;
        double secondX = 0;
        double secondY = 0;

        for(int i = 0;  i < (path.size() - 1); i++) {
            lineColor = path.get(i).getFloor().equals(floor) ? Color.RED : Color.GRAY;

            firstX = transformX(path.get(i).getX(), nodePane.getPrefWidth());
            firstY = transformY(path.get(i).getY(), nodePane.getPrefHeight());
            secondX = transformX(path.get(i + 1).getX(), nodePane.getPrefWidth());
            secondY = transformY(path.get(i + 1).getY(), nodePane.getPrefHeight());
            Line line = createLine(firstX, firstY, secondX, secondY, null);
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
                double tfStartX = transformX(startNode.getXPos(), nodePane.getPrefWidth());
                double tfStartY = transformY(startNode.getYPos(), nodePane.getPrefHeight());
                double tfEndX = transformX(endNode.getXPos(), nodePane.getPrefWidth());
                double tfEndY = transformY(endNode.getYPos(), nodePane.getPrefHeight());
                Line line = createLine(tfStartX, tfStartY, tfEndX, tfEndY, edge);
                nodePane.getChildren().add(line);
            }
        }

    }

    private Line createLine(double StartX, double StartY, double EndX, double EndY, EdgeInfo edge) {
        Line line = new Line(StartX, StartY, EndX, EndY);
        line.setStroke(lineColor);
        line.setStrokeWidth(4);

        line.setOnMouseClicked(event -> {
            onEdgeClicked.accept(edge);
            event.consume();
        });
        line.setOnMouseEntered(event -> {
            line.setStroke(Color.GREEN);
            event.consume();
        });
        line.setOnMouseExited(event -> {
            line.setStroke(Color.RED);
            event.consume();
        });

        return line;
    }

    private void createCircles(List<NodeInfo> nodes) {
        for (NodeInfo node : nodes) {
            double transformedX = transformX(node.getXPos(), nodePane.getPrefWidth());
            double transformedY = transformY(node.getYPos(), nodePane.getPrefHeight());
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

    private static double transformX(int nodeX, double paneWidth) {
        return nodeX * (paneWidth / imageWidth);
    }

    private static double transformY(int nodeY, double paneHeight) {
        return nodeY * (paneHeight / imageHeight);
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

    static public boolean loadImages() {
        boolean loaded = true;
        try {
            FileInputStream L1FloorStream = new FileInputStream("src/main/resources/edu/wpi/teamo/fxml/Maps/00_thelowerlevel1.png");
            L1FloorImage = new Image(L1FloorStream);

            FileInputStream L2FloorStream = new FileInputStream("src/main/resources/edu/wpi/teamo/fxml/Maps/00_thelowerlevel2.png");
            L2FloorImage = new Image(L2FloorStream);

            FileInputStream groundFloorStream = new FileInputStream("src/main/resources/edu/wpi/teamo/fxml/Maps/00_thegroundfloor.png");
            groundFloorImage = new Image(groundFloorStream);

            FileInputStream firstFloorStream = new FileInputStream("src/main/resources/edu/wpi/teamo/fxml/Maps/01_thefirstfloor.png");
            firstFloorImage = new Image(firstFloorStream);

            FileInputStream secondFloorStream = new FileInputStream("src/main/resources/edu/wpi/teamo/fxml/Maps/02_thesecondfloor.png");
            secondFloorImage = new Image(secondFloorStream);

            FileInputStream thirdFloorStream = new FileInputStream("src/main/resources/edu/wpi/teamo/fxml/Maps/03_thethirdfloor.png");
            thirdFloorImage = new Image(thirdFloorStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            loaded = false;
        }
        return loaded;
    }
}
