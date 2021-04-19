package edu.wpi.teamo.views;

import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.scene.canvas.GraphicsContext;
import java.io.FileNotFoundException;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

public class Map {

    ImageView imageView;
    GraphicsContext gc;
    Canvas canvas;

    static final int imageWidth = 5000;
    static final int imageHeight = 3400;
    private boolean imagesLoaded = true;

    Image L1FloorImage = null;
    Image L2FloorImage = null;
    Image groundFloorImage = null;
    Image firstFloorImage = null;
    Image secondFloorImage = null;
    Image thirdFloorImage = null;

    public Map(Canvas canvas, ImageView imageView) {
        this.gc = canvas.getGraphicsContext2D();
        imagesLoaded = loadImages();
        this.imageView = imageView;
        this.canvas = canvas;

        gc.setStroke(Color.RED);
        gc.setFill(Color.BLUE);
        gc.setLineWidth(0.5);

    }

    public void drawFloor(List<NodeInfo> allNodes, List<EdgeInfo> edges, String floor) {
        /* filter only the nodes belonging to this floor */
        List<NodeInfo> floorNodes = allNodes.stream()
                                            .filter((NodeInfo node) -> node.getFloor().equals(floor))
                                            .collect(Collectors.toList());

        switchFloorImage(floor);

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawEdges(gc, floorNodes, edges, canvas.getWidth(), canvas.getHeight());
        drawNodes(gc, floorNodes, canvas.getWidth(), canvas.getHeight());
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
                case "1":
                    imageView.setImage(groundFloorImage);
                    break;
                case "2":
                    imageView.setImage(firstFloorImage);
                    break;
                case "3":
                    imageView.setImage(secondFloorImage);
                    break;
                case "4":
                default:
                    imageView.setImage(thirdFloorImage);
                    break;
            }
        }
        else {
            System.out.println("ERROR: IMAGES NOT LOADED, CANNOT SWITCH FLOOR IMAGE");
        }
    }

    private static void drawNodes(GraphicsContext gc, List<NodeInfo> nodes,
                                  double canvasWidth, double canvasHeight) {
        for (NodeInfo node : nodes) {
            double transformedX = transformX(node.getXPos(), canvasWidth);
            double transformedY = transformY(node.getYPos(), canvasHeight);

            gc.fillOval(transformedX - 1.5, transformedY - 1.5, 3, 3);
        }
    }

    private static void drawEdges(GraphicsContext gc, List<NodeInfo> nodes,
                                  List<EdgeInfo> edges, double canvasWidth, double canvasHeight) {
        for (EdgeInfo edge : edges) {
            NodeInfo startNode = findNode(edge.getStartNodeID(), nodes);
            NodeInfo endNode = findNode(edge.getEndNodeID(), nodes);

            if (startNode != null && endNode != null) {
                double tfStartX = transformX(startNode.getXPos(), canvasWidth);
                double tfStartY = transformY(startNode.getYPos(), canvasHeight);
                double tfEndX = transformX(endNode.getXPos(), canvasWidth);
                double tfEndY = transformY(endNode.getYPos(), canvasHeight);
                gc.strokeLine(tfStartX, tfStartY, tfEndX, tfEndY);
            }
        }
    }

    private boolean loadImages() {
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

    private static NodeInfo findNode(String id, List<NodeInfo> nodes) {
        NodeInfo foundNode = null;

        for (NodeInfo node : nodes) {
            if (node.getNodeID().equals(id)) {
                foundNode = node;
            }
        }

        return foundNode;
    }

    private static double transformX(int nodeX, double canvasWidth) {
        return nodeX * (canvasWidth / imageWidth);
    }

    private static double transformY(int nodeY, double canvasHeight) {
        return nodeY * (canvasHeight / imageHeight);
    }

}
