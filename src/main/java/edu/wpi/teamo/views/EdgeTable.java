package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.IMapService;
import edu.wpi.teamo.database.map.EdgeInfo;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.Locale;

public class EdgeTable extends TableSearcher<EdgeInfo, HBox> {

    private final IMapService mapService;

    double columnWidth = 200;

    public interface OnUpdate {
        void onUpdate();
    }
    OnUpdate onUpdate = null;

    public EdgeTable(JFXTextField searchBar, JFXListView<HBox> table, OnUpdate onUpdate, IMapService mapService) {
        super(searchBar, table, null, null, null);
        setHeaderCellCreator(this::createHeader);
        setCellCreator(this::createRow);
        setMatcher(this::nodeMatches);
        this.mapService = mapService;
        this.onUpdate = onUpdate;
    }

    private boolean nodeMatches(EdgeInfo edge, String text) {
        boolean matching = false;

        text = text.toLowerCase(Locale.ROOT);

        matching |= edge.getEdgeID().toLowerCase(Locale.ROOT).contains(text);
        matching |= edge.getStartNodeID().toLowerCase(Locale.ROOT).contains(text);
        matching |= edge.getEndNodeID().toLowerCase(Locale.ROOT).contains(text);

        return matching;
    }

    private HBox createHeader() {
        Label idField = new Label("id");
        idField.setMinWidth(columnWidth);
        Label startNodeIDField = new Label("startNodeID");
        startNodeIDField.setMinWidth(columnWidth);
        Label endNodeIDField = new Label("endNodeID");
        endNodeIDField.setMinWidth(columnWidth);

        HBox row = new HBox(idField, startNodeIDField, endNodeIDField);
        return row;
    }

    private HBox createRow(EdgeInfo edge, boolean isSelected) {

        JFXTextField idField = new JFXTextField();
        idField.setText(edge.getEdgeID());
        idField.setOnAction(event -> handleIDChange(idField, edge));
        idField.setMinWidth(columnWidth);

        JFXTextField startIDField = new JFXTextField();
        startIDField.setText(edge.getStartNodeID());
        startIDField.setOnAction(event -> handleStartIDChange(startIDField, edge));
        startIDField.setMinWidth(columnWidth);

        JFXTextField endIDField = new JFXTextField();
        endIDField.setText(edge.getEndNodeID());
        endIDField.setOnAction(event -> handleEndIDChange(endIDField, edge));
        endIDField.setMinWidth(columnWidth);

        HBox row = new HBox(idField, startIDField, endIDField);
        row.setStyle("-fx-background-color: rgba(132,252,248, 0.4)");

        return row;
    }


    private void handleStartIDChange(JFXTextField textField, EdgeInfo edge) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setEdgeStartID(edge.getEdgeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(edge.getStartNodeID());
            }
        }
    }

    private void handleEndIDChange(JFXTextField textField, EdgeInfo edge) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setEdgeEndID(edge.getEdgeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(edge.getEndNodeID());
            }
        }
    }

    private void handleIDChange(JFXTextField textField, EdgeInfo edge) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                if (mapService.edgeExists(text)) throw new SQLException();
                mapService.setNodeID(edge.getEdgeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(edge.getEdgeID());
            }
        }
    }

}
