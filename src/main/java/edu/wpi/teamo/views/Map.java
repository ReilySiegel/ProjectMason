package edu.wpi.teamo.views;

import edu.wpi.teamo.App;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.NodeInfo;
import com.jfoenix.controls.JFXTextArea;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import java.io.FileNotFoundException;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import edu.wpi.teamo.algos.AlgoNode;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import java.util.LinkedList;

import javafx.scene.shape.Polygon;
import javafx.util.Pair;
import java.util.List;

public class Map  {

    private Consumer<Pair<Circle, NodeInfo>> onDrawNode = null;
    private Consumer<Pair<Line, EdgeInfo>> onDrawEdge = null;

    static private boolean imagesLoaded = false;

    static public final int mapHeight = 3400;
    static public final int mapWidth = 5000;
    static public final double mapAspectRatio = (double) mapWidth / mapHeight;
    static public final double paneHeight = 720;
    static public final double paneWidth = paneHeight * mapAspectRatio;

    static public final double maxScale = 10;
    static public final double minScale = 0.5;

    static Image groundFloorImage = null;
    static Image secondFloorImage = null;
    static Image firstFloorImage = null;
    static Image thirdFloorImage = null;
    static Image L1FloorImage = null;
    static Image L2FloorImage = null;

    public static final String floorL2Key = "L2";
    public static final String floorL1Key = "L1";
    public static final String floorGKey = "G";
    public static final String floor1Key = "1";
    public static final String floor2Key = "2";
    public static final String floor3Key = "3";
    public static final String[] floorKeys = { floorL2Key, floorL1Key, floorGKey, floor1Key, floor2Key, floor3Key };

    private final AnchorPane nodePane;

    /* variables used to calculate the zoom and pan transforms */
    private Double initialTranslateX = null;
    private Double initialTranslateY = null;
    private Double initialScreenX = null;
    private Double initialScreenY = null;
    private boolean dragging = false;
    private double scale = 1;

    private Paint otherFloorPathColor = Color.GRAY;
    private Paint sameFloorPathColor = Color.RED;
    private Paint circleColor = Color.BLUE;
    private Paint lineColor = Color.RED;

    public double defaultStrokeWidth = 5;
    public double defaultRadius = 5;

    public List<Circle> circles;
    public List<Line> lines;

    public Map(AnchorPane nodePane) {
        this.nodePane = nodePane;
        setNodePaneSize(paneWidth, paneHeight);
        nodePane.setOnMouseDragged((MouseEvent e) -> handleDrag(e.getScreenX(), e.getScreenY()));
        nodePane.setOnMouseReleased((MouseEvent e) -> resetInitialDragData());
        nodePane.setOnScroll((ScrollEvent e) -> scaleMap(e.getDeltaY()));

        double middleOfPageMinusHalfPaneWidth  = (App.getPrimaryStage().getScene().getWidth()  / 2) - (paneWidth  / 2);
        double middleOfPageMinusHalfPaneHeight = (App.getPrimaryStage().getScene().getHeight() / 2) - (paneHeight / 2);

        setMapTranslate(middleOfPageMinusHalfPaneWidth, middleOfPageMinusHalfPaneHeight);
        resetInitialDragData();
//        scaleMap(300);
    }

    public double getHeight() {
        return paneHeight;
    }

    public double getWidth() {
        return paneWidth;
    }

    private void setNodePaneSize(double x, double y) {
        nodePane.setMinHeight(y);
        nodePane.setMaxHeight(y);
        nodePane.setMinWidth(x);
        nodePane.setMaxWidth(x);
    }

    public void drawPath(LinkedList<AlgoNode> path, String floor,int index) {
        lines = new LinkedList<>();
        for(int i = 0;  i < (path.size() - 1); i++) {
            Paint lineColor = path.get(i).getFloor().equals(floor) ? sameFloorPathColor : otherFloorPathColor;

            double firstX  = mapToPaneX(path.get(i).getX());
            double firstY  = mapToPaneY(path.get(i).getY());
            double secondX = mapToPaneX(path.get(i + 1).getX());
            double secondY = mapToPaneY(path.get(i + 1).getY());

            Line line;
            if(index  == i)
            {
                line = createLine(firstX, firstY, secondX, secondY, null,Color.BLUE);
                createTriangle(firstX,firstY,secondX,secondY,6,Color.BLUE);
            }
            else{
                line = createLine(firstX, firstY, secondX, secondY, null, lineColor);
                createTriangle(firstX,firstY,secondX,secondY,4,lineColor);
            }
            nodePane.getChildren().add(line);
            lines.add(line);

            if (i == 0) {
                nodePane.getChildren().add(new Circle(firstX, firstY, defaultRadius * 2, circleColor));
            }
            else if (i == path.size() - 2) {
                nodePane.getChildren().add(new Circle(secondX, secondY, defaultRadius * 2, circleColor));
            }

        }

        scaleMap(0);
    }

    public void drawNodes(List<NodeInfo> allNodes, String floor) {
        List<NodeInfo> floorNodes = filterFloor(allNodes, floor);

        switchFloorImage(floor);

        addCircles(floorNodes);

        scaleMap(0);
    }


    public void drawEdges(List<NodeInfo> allNodes, List<EdgeInfo> edges, String floor) {
        List<NodeInfo> floorNodes = filterFloor(allNodes, floor);

        switchFloorImage(floor);

        addLines(edges, floorNodes);

        scaleMap(0);
    }

    public void clearShapes() {
        nodePane.getChildren().removeAll(nodePane.getChildren());
    }

    private void addLines(List<EdgeInfo> edges, List<NodeInfo> nodes) {
        lines = new LinkedList<>();

        for (EdgeInfo edge : edges) {
            NodeInfo startNode = findNode(edge.getStartNodeID(), nodes);
            NodeInfo endNode = findNode(edge.getEndNodeID(), nodes);

            if (startNode != null && endNode != null) {
                double tfStartX = mapToPaneX(startNode.getXPos());
                double tfStartY = mapToPaneY(startNode.getYPos());
                double tfEndX = mapToPaneX(endNode.getXPos());
                double tfEndY = mapToPaneY(endNode.getYPos());
                Line line = createLine(tfStartX, tfStartY, tfEndX, tfEndY, edge, lineColor);
                if (line != null) {
                    nodePane.getChildren().add(line);
                    lines.add(line);
                }
            }
        }

    }

    private Line createLine(double StartX, double StartY, double EndX, double EndY, EdgeInfo edge, Paint lineColor) {
        Line line = new Line(StartX, StartY, EndX, EndY);
        line.setStroke(lineColor);
        line.setStrokeWidth(defaultStrokeWidth);

        if (onDrawEdge != null) {
            onDrawEdge.accept(new Pair<>(line, edge));
        }

        if (isWithinPaneBounds(StartX, StartY) && isWithinPaneBounds(EndX, EndY)) {
            return line;
        }
        else {
            return null;
        }
    }

    private void addCircles(List<NodeInfo> nodes) {
        circles = new LinkedList<>();
        for (NodeInfo node : nodes) {
            double transformedX = mapToPaneX(node.getXPos());
            double transformedY = mapToPaneY(node.getYPos());
            Circle circle = createCircle(transformedX, transformedY, node);
            if (circle != null) {
                nodePane.getChildren().add(circle);
                circles.add(circle);
            }
        }
    }

    private Circle createCircle(double x, double y, NodeInfo node) {
        Circle circle = new Circle(x, y, defaultRadius, circleColor);

        if (onDrawNode != null) {
            onDrawNode.accept(new Pair<>(circle, node));
        }

        if (isWithinPaneBounds(x, y)) {
            return circle;
        }
        else {
            return null;
        }
    }
    public void createTriangle(double beginx, double beginy, double endx, double endy,double size, Paint lineColor )
    {
        double from, to, transX , transY, dist_tri_center;

        double halfX= (endx-beginx)/2;
        double halfY = (endy-beginy)/2;
        double rads = Math.atan2((endy-beginy),(endx -beginx));
        double distance = Math.sqrt(Math.pow((endx-beginx),2)+Math.pow(endy-beginy,2));

        transX = halfX+beginx;
        transY= halfY+beginy;
        //dist_tri_center = 3;
        Polygon tri = drawTriangle(transX, transY,size,rads,lineColor);

        if(distance > 15)
        {
            nodePane.getChildren().add(tri);
        }



    }

    public Polygon drawTriangle(double x, double y, double size, double radians, Paint color)
    {
        double firstX,firstY,secondX,secondY,thirdX,thirdY;
        double[] corn = new double[6];
        firstX  =x +size *Math.cos(radians);
        firstY = y+size*Math.sin(radians);
        secondX = x+size*Math.cos(radians+2*Math.PI/3);
        secondY = y+size*Math.sin(radians+2*Math.PI/3);
        thirdX = x+size*Math.cos(radians+4*Math.PI/3);
        thirdY = y+size*Math.sin(radians+4*Math.PI/3);
        corn[0] = firstX;
        corn[1] = firstY;
        corn[2] = secondX;
        corn[3] = secondY;
        corn[4] = thirdX;
        corn[5] = thirdY;
        Polygon p = new Polygon(corn);
        p.setStroke(color);
        p.setFill(color);

        if (isWithinPaneBounds(x, y) && isWithinPaneBounds(x,y)) {
            return p;
        }
        else {
            return null;
        }

    }


    private boolean isWithinPaneBounds(double x, double y) {
        boolean inBounds = true;
        if (x < 0 || x > getWidth()) inBounds = false;
        if (y < 0 || y > getHeight()) inBounds = false;
        return inBounds;
    }

    public static boolean isWithinMapBounds(int x, int y) {
        boolean inBounds = true;
        if (x < 0 || x > mapWidth)  inBounds = false;
        if (y < 0 || y > mapHeight) inBounds = false;
        return inBounds;
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

    public double mapToPaneX(int val) {
        return transform(val, mapWidth, getWidth());
    }

    public double mapToPaneY(int val) {
        return transform(val, mapHeight, getHeight());
    }

    public int paneToMapX(double val) {
        return (int) transform(val, getWidth(), mapWidth);
    }

    public int paneToMapY(double val) {
        return (int) transform(val, getHeight(), mapHeight);
    }

    public static double transform(double val, double from, double to) {
        return val * (to / from);
    }


    private void resetInitialDragData() {
        initialTranslateX = null;
        initialTranslateY = null;
        initialScreenX = null;
        initialScreenY = null;
        dragging = false;
    }

    public void handleDrag(double screenX, double screenY) {
        /* if the drag was just initiated, must save initial conditions */
        if (initialTranslateX == null) {
            initialTranslateX = nodePane.getTranslateX();
            initialTranslateY = nodePane.getTranslateY();
            initialScreenX = screenX;
            initialScreenY = screenY;
            dragging = true;
        }
        /* translate by the displacement of the mouse since initial click */
        else {
            double screenDisplacementX = screenX - initialScreenX;
            double screenDisplacementY = screenY - initialScreenY;

            setMapTranslate(
                    initialTranslateX + screenDisplacementX,
                    initialTranslateY + screenDisplacementY
            );
        }
    }

    public void setMapTranslate(double x,double y) {
        nodePane.setTranslateX(x);
        nodePane.setTranslateY(y);
    }

    public void scaleMap(double scroll) {
        /* scroll is proportional to current scale to keep zoom speed the same */
        double dS = scroll * scale / 500;
        scale += dS;

        if (scale > maxScale) {
            scale = maxScale;
        }
        else if (scale < minScale) {
            scale = minScale;
        }
        else {

            /* scale the views from the center */
            nodePane.setScaleX(scale);
            nodePane.setScaleY(scale);

            for (javafx.scene.Node thing : nodePane.getChildren()) {
                if (thing.getClass() == Circle.class) {
                    ((Circle) thing).setRadius(defaultRadius / scale);
                } else if (thing.getClass() == Line.class) {
                    ((Line) thing).setStrokeWidth(defaultStrokeWidth / scale);
                }
            }


            /* must translate based on how much the current point moved from the scale */
            nodePane.setTranslateX(nodePane.getTranslateX() + (nodePane.getTranslateX() / scale) * dS);
            nodePane.setTranslateY(nodePane.getTranslateY() + (nodePane.getTranslateY() / scale) * dS);

        }
    }

    public void setOnMouseMoved(Consumer<Pair<Double, Double>> onMouseMoved) {
        nodePane.setOnMouseMoved((MouseEvent e) -> {
            if (onMouseMoved != null) {
                onMouseMoved.accept(new Pair<>(e.getX(), e.getY()));
            }
        });
    }

    public void setOnMapClicked(Consumer<MouseEvent> onMapClicked) {
        nodePane.setOnMouseClicked((MouseEvent e) -> {
            if (!dragging) onMapClicked.accept(e);
            resetInitialDragData();
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
            Image imageToSet = null;
            switch (floor) {
                case floorL1Key:
                    imageToSet = L1FloorImage;
                    break;
                case floorL2Key:
                    imageToSet = L2FloorImage;
                    break;
                case floorGKey:
                    imageToSet = groundFloorImage;
                    break;
                case floor1Key:
                    imageToSet = firstFloorImage;
                    break;
                case floor2Key:
                    imageToSet = secondFloorImage;
                    break;
                case floor3Key:
                    imageToSet = thirdFloorImage;
                    break;
            }

            if (imageToSet != null) {

//                ImageView imageView = new ImageView(imageToSet);
//                imageView.setPreserveRatio(false);
//                imageView.setFitWidth(nodePane.getWidth());
//                imageView.setFitHeight(nodePane.getHeight());
//                imageToSet = imageView.snapshot(null, null);

                BackgroundSize brs = new BackgroundSize(100, 100, true, true, true, false);
                BackgroundImage bri = new BackgroundImage(imageToSet, null, null, null, brs);
                nodePane.setBackground(new Background(bri));
            }
            else {
                throw new Error("imageToSet not set!");
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

    public void showNodes() {
        for (Circle circle : circles) {
            circle.setVisible(true);
        }
    }

    public void hideNodes() {
        for (Circle circle : circles) {{
            circle.setVisible(false);
        }}
    }

    public Paint getOtherFloorPathColor() {
        return otherFloorPathColor;
    }

    public void setOtherFloorPathColor(Paint otherFloorPathColor) {
        this.otherFloorPathColor = otherFloorPathColor;
    }

    public Paint getSameFloorPathColor() {
        return sameFloorPathColor;
    }

    public void setSameFloorPathColor(Paint sameFloorPathColor) {
        this.sameFloorPathColor = sameFloorPathColor;
    }

    public Paint getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(Paint circleColor) {
        this.circleColor = circleColor;
    }

    public Paint getLineColor() {
        return lineColor;
    }

    public void setLineColor(Paint lineColor) {
        this.lineColor = lineColor;
    }

    public void centerOnMapCoords(int x, int y) {
        /* convert to nodePane coords */
        double pX = mapToPaneX(x);
        double pY = mapToPaneY(y);

        /* convert coords to coords relative to center of nodePane */
        pX = pX - getWidth()  / 2;
        pY = pY - getHeight() / 2;

        /* scale */
        pX *= scale;
        pY *= scale;

        /* translate pane to center of screen */
        double tX = App.getPrimaryStage().getScene().getWidth()  / 2 - getWidth() / 2;
        double tY = App.getPrimaryStage().getScene().getHeight() / 2 - getHeight() / 2;

        /* translate by the node coords */
        tX = tX - pX;
        tY = tY - pY;

        /* translate */
        setMapTranslate(tX, tY);
    }
}
