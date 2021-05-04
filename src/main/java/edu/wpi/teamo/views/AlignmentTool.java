package edu.wpi.teamo.views;

import edu.wpi.teamo.database.map.IMapService;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AlignmentTool {

    private final HashMap<String, List<Line>> associatedEdgeBeginnings;
    private final HashMap<String, List<Line>> associatedEdgeEndings;
    private final IMapService mapService;
    private final List<Alignee> alignees;
    private final double pivotX;
    private final double pivotY;
    private final Map map;

    static class Alignee {
        public double pivotRadius;
        public Circle circle;
        public NodeInfo node;
        public Alignee(Circle circle, NodeInfo node, double pivotRadius) {
            this.pivotRadius = pivotRadius;
            this.circle = circle;
            this.node = node;
        }
    }

    public AlignmentTool(HashMap<String, List<Line>> associatedEdgeBeginnings,
                         HashMap<String, List<Line>> associatedEdgeEndings,
                         double pivotX,
                         double pivotY,
                         IMapService mapService,
                         Map map) {
        this.associatedEdgeBeginnings = associatedEdgeBeginnings;
        this.associatedEdgeEndings = associatedEdgeEndings;
        alignees = new LinkedList<>();
        this.mapService = mapService;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.map = map;
    }

    public void addAlignee(Circle circle, NodeInfo node) {
        double dX = circle.getCenterX() - pivotX;
        double dY = circle.getCenterY() - pivotY;
        double radius = Math.sqrt( Math.pow(dX, 2) + Math.pow(dY, 2) );
        alignees.add(new Alignee(circle, node, radius));
    }

    public void onMouseMoved(double x, double y) {
        double pivotToMouseX = x - pivotX;
        double pivotToMouseY = y - pivotY;
        double pivotToMouseA = Math.atan2(pivotToMouseY, pivotToMouseX);

        for (Alignee alignee : alignees) {
            double pivotToAligneeX = alignee.pivotRadius * Math.cos(pivotToMouseA);
            double pivotToAligneeY = alignee.pivotRadius * Math.sin(pivotToMouseA);
            alignee.circle.setCenterX(this.pivotX + pivotToAligneeX);
            alignee.circle.setCenterY(this.pivotY + pivotToAligneeY);
            moveLines(alignee);
        }
    }

    public void confirmAlignment() throws SQLException {
        for (Alignee alignee : alignees) {
            int mapX = (int) Map.transform((int) alignee.circle.getCenterX(), map.getWidth(), Map.mapWidth);
            int mapY = (int) Map.transform((int) alignee.circle.getCenterY(), map.getHeight(), Map.mapHeight);
            mapService.setNodePosition(alignee.node.getNodeID(), mapX, mapY);
        }
    }

    public void moveLines(Alignee alignee) {
        try {
            List<Line> lineBeginnings = associatedEdgeBeginnings.get(alignee.node.getNodeID());
            if (lineBeginnings != null) {
                for (Line line : lineBeginnings) {
                    line.setStartX(alignee.circle.getCenterX());
                    line.setStartY(alignee.circle.getCenterY());
                }
            }
            List<Line> lineEndings = associatedEdgeEndings.get(alignee.node.getNodeID());
            if (lineEndings != null) {
                for (Line line : lineEndings) {
                    line.setEndX(alignee.circle.getCenterX());
                    line.setEndY(alignee.circle.getCenterY());
                }
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }

}
