package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.database.map.IMapService;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.Locale;

public class NodeTable extends TableSearcher<NodeInfo, HBox> {

    private final IMapService mapService;

    double columnWidth = 200;

    public static interface OnUpdate {
        void onUpdate();
    }
    OnUpdate onUpdate = null;

    public NodeTable(JFXTextField searchBar, JFXListView<HBox> table, OnUpdate onUpdate, IMapService mapService) {
        super(searchBar, table, null, null, null);
        setHeaderCellCreator(this::createHeader);
        setCellCreator(this::createRow);
        setMatcher(this::nodeMatches);
        this.mapService = mapService;
        this.onUpdate = onUpdate;
    }

    private boolean nodeMatches(NodeInfo node, String text) {
        boolean matching;

        text = text.toLowerCase(Locale.ROOT);

        matching  = String.valueOf(node.getXPos()).toLowerCase(Locale.ROOT).contains(text);
        matching |= String.valueOf(node.getYPos()).toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getShortName().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getNodeType().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getBuilding().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getLongName().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getNodeID().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getFloor().toLowerCase(Locale.ROOT).contains(text);

        return matching;
    }

    private HBox createHeader() {
        Label idField = new Label("id");
        idField.setMinWidth(columnWidth);
        Label longNameField = new Label("longName");
        longNameField.setMinWidth(columnWidth);
        Label shortNameField = new Label("shortName");
        shortNameField.setMinWidth(columnWidth);
        Label typeField = new Label("type");
        typeField.setMinWidth(columnWidth);
        Label buildingField = new Label("building");
        buildingField.setMinWidth(columnWidth);
        Label floorField = new Label("floor");
        floorField.setMinWidth(columnWidth);
        Label xField = new Label("x");
        xField.setMinWidth(columnWidth);
        Label yField = new Label("y");
        yField.setMinWidth(columnWidth);
        HBox row = new HBox(idField, longNameField, shortNameField, typeField, buildingField, floorField, xField, yField);
        return row;
    }

    private HBox createRow(NodeInfo node, boolean isSelected) {

        JFXTextField idField = new JFXTextField();
        idField.setText(node.getNodeID());
        idField.setOnAction(event -> handleIDChange(idField, node));
        idField.setMinWidth(columnWidth);

        JFXTextField longNameField = new JFXTextField();
        longNameField.setText(node.getLongName());
        longNameField.setOnAction(event -> handleLNChange(longNameField, node));
        longNameField.setMinWidth(columnWidth);

        JFXTextField shortNameField = new JFXTextField();
        shortNameField.setText(node.getShortName());
        shortNameField.setOnAction(event -> handleSNChange(shortNameField, node));
        shortNameField.setMinWidth(columnWidth);

        JFXTextField typeField = new JFXTextField();
        typeField.setText(node.getNodeType());
        typeField.setOnAction(event -> handleTypeChange(typeField, node));
        typeField.setMinWidth(columnWidth);

        JFXTextField buildingField = new JFXTextField();
        buildingField.setText(node.getBuilding());
        buildingField.setOnAction(event -> handleBuildingChange(buildingField, node));
        buildingField.setMinWidth(columnWidth);

        JFXTextField floorField = new JFXTextField();
        floorField.setText(node.getFloor());
        floorField.setOnAction(event -> handleFloorChange(floorField, node));
        floorField.setMinWidth(columnWidth);

        JFXTextField xField = new JFXTextField();
        xField.setText(String.valueOf(node.getXPos()));
        xField.setOnAction(event -> handleXChange(xField, node));
        xField.setMinWidth(columnWidth);

        JFXTextField yField = new JFXTextField();
        yField.setText(String.valueOf(node.getYPos()));
        yField.setOnAction(event -> handleYChange(yField, node));
        yField.setMinWidth(columnWidth);

        HBox row = new HBox(idField, longNameField, shortNameField, typeField, buildingField, floorField, xField, yField);
        row.setStyle("-fx-background-color: rgba(132,252,248, 0.4)");

        return row;
    }

    private void handleYChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                int newY = Integer.parseInt(text);
                mapService.setNodePosition(node.getNodeID(), node.getXPos(), newY);
                onUpdate.onUpdate();
            } catch (NumberFormatException e) {
                textField.setText(String.valueOf(node.getYPos()));
            } catch (SQLException throwables) {
                textField.setText(String.valueOf(node.getYPos()));
                throwables.printStackTrace();
            }
        }
    }

    private void handleXChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                int newX = Integer.parseInt(text);
                mapService.setNodePosition(node.getNodeID(), newX, node.getYPos());
                onUpdate.onUpdate();
            } catch (NumberFormatException e) {
                textField.setText(String.valueOf(node.getXPos()));
            } catch (SQLException throwables) {
                textField.setText(String.valueOf(node.getXPos()));
                throwables.printStackTrace();
            }
        }
    }

    private void handleFloorChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setNodeFloor(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getFloor());
            }
        }
    }

    private void handleBuildingChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setNodeBuilding(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getBuilding());
            }
        }
    }

    private void handleTypeChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setNodeType(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getNodeType());
            }
        }
    }

    private void handleSNChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setNodeShortName(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getShortName());
            }
        }
    }

    private void handleIDChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                if (mapService.nodeExists(text)) throw new SQLException();
                mapService.setNodeID(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getNodeID());
            }
        }
    }

    private void handleLNChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setNodeLongName(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getLongName());
            }
        }
    }


}
