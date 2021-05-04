package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.IMapService;
import edu.wpi.teamo.database.map.EdgeInfo;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class EdgeTable extends TableSearcher<EdgeInfo, HBox> {

    private final IMapService mapService;

    private JFXComboBox<String> validityFilter = null;
    private JFXComboBox<String> floorFilter = null;

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

        //TODO: filter edges by floor

        if (!validityFilter.getValue().equals(MapEditorPage.allValidityKey)) {
            matching &= ( edge.isValid() && validityFilter.getValue().equals(MapEditorPage.validKey  )) ||
                        (!edge.isValid() && validityFilter.getValue().equals(MapEditorPage.invalidKey));
        }

        return matching;
    }

    private HBox createHeader() {
        List<Label> columns = new LinkedList<>();

        columns.add(new Label("id"));
        columns.add(new Label("startNodeID"));
        columns.add(new Label("endNodeID"));

        columns.forEach(column -> column.setMinWidth(columnWidth));
        HBox row = new HBox();
        row.getChildren().addAll(columns);

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

        if (edge.isValid()) {
            row.setStyle("-fx-background-color: rgba(132,252,248, 0.4)");
        }
        else {
            row.setStyle("-fx-background-color: rgb(252,132,132)");
        }

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

    public void setFloorFilter(JFXComboBox floorFilter) {
        this.floorFilter = floorFilter;
    }

    public void setValidityFilter(JFXComboBox validityFilter) {
        this.validityFilter = validityFilter;
    }
}
